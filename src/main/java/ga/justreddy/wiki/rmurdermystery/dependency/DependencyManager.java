package ga.justreddy.wiki.rmurdermystery.dependency;

import com.google.common.collect.ImmutableSet;
import ga.justreddy.wiki.rmurdermystery.MurderMystery;
import ga.justreddy.wiki.rmurdermystery.dependency.classloader.IsolatedClassLoader;
import ga.justreddy.wiki.rmurdermystery.dependency.relocation.Relocation;
import ga.justreddy.wiki.rmurdermystery.dependency.relocation.RelocationHandler;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

public class DependencyManager {

    /**
     * The plugin instance
     */
    private final MurderMystery plugin;
    /**
     * A registry containing plugin specific behaviour for dependencies.
     */
    private final DependencyRegistry registry;
    /**
     * The path where library jars are cached.
     */
    private final Path cacheDirectory;

    /**
     * A map of dependencies which have already been loaded.
     */
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);
    /**
     * A map of isolated classloaders which have been created.
     */
    private final Map<ImmutableSet<Dependency>, IsolatedClassLoader> loaders = new HashMap<>();
    /**
     * Cached relocation handler instance.
     */
    private @MonotonicNonNull RelocationHandler relocationHandler = null;

    public DependencyManager(MurderMystery plugin) {
        this.plugin = plugin;
        this.registry = new DependencyRegistry(plugin);
        this.cacheDirectory = setupCacheDirectory(plugin);
    }

    private synchronized RelocationHandler getRelocationHandler() {
        if (this.relocationHandler == null) {
            this.relocationHandler = new RelocationHandler(this);
        }
        return this.relocationHandler;
    }

    public IsolatedClassLoader obtainClassLoaderWith(Set<Dependency> dependencies) {
        ImmutableSet<Dependency> set = ImmutableSet.copyOf(dependencies);

        for (Dependency dependency : dependencies) {
            if (!this.loaded.containsKey(dependency)) {
                throw new IllegalStateException("Dependency " + dependency + " is not loaded.");
            }
        }

        synchronized (this.loaders) {
            IsolatedClassLoader classLoader = this.loaders.get(set);
            if (classLoader != null) {
                return classLoader;
            }

            URL[] urls = set.stream()
                    .map(this.loaded::get)
                    .map(file -> {
                        try {
                            return file.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            classLoader = new IsolatedClassLoader(urls);
            this.loaders.put(set, classLoader);
            return classLoader;
        }
    }

    public void loadDependencies(Dependency... dependencies){
        Set<Dependency> dependencies1 = new HashSet<>(Arrays.asList(dependencies));
        loadDependencies(dependencies1);
    }

    public void loadDependencies(Set<Dependency> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            try {
                loadDependency(dependency);
            } catch (Throwable e) {
                this.plugin.getLogger().log(Level.SEVERE, "Unable to load dependency " + dependency.name() + ".", e);
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loadDependency(Dependency dependency) throws Exception {
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = remapDependency(dependency, downloadDependency(dependency));

        this.loaded.put(dependency, file);

    }


    public Path downloadDependency(Dependency dependency) throws DependencyDownloadException {
        Path file = this.cacheDirectory.resolve(dependency.getFileName(null));

        // if the file already exists, don't attempt to re-download it.
        if (Files.exists(file)) {
            return file;
        }

        DependencyDownloadException lastError = null;

        // attempt to download the dependency from each repo in order.
        for (DependencyRepository repo : DependencyRepository.values()) {
            try {
                repo.download(dependency, file);
                return file;
            } catch (DependencyDownloadException e) {
                lastError = e;
            }
        }

        throw Objects.requireNonNull(lastError);
    }

    private Path remapDependency(Dependency dependency, Path normalFile) throws Exception {
        List<Relocation> rules = new ArrayList<>(dependency.getRelocations());
        this.registry.applyRelocationSettings(dependency, rules);

        if (rules.isEmpty()) {
            return normalFile;
        }

        Path remappedFile = this.cacheDirectory.resolve(dependency.getFileName(DependencyRegistry.isGsonRelocated() ? "remapped-legacy" : "remapped"));

        // if the remapped source exists already, just use that.
        if (Files.exists(remappedFile)) {
            return remappedFile;
        }

        getRelocationHandler().remap(normalFile, remappedFile, rules);
        return remappedFile;
    }

    private static Path setupCacheDirectory(MurderMystery plugin) {
        Path cacheDirectory = new File("plugins/" + plugin.getDescription().getName() + "/libs").toPath();
        try {
            MoreFiles.createDirectoriesIfNotExists(cacheDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create libs directory", e);
        }

        Path oldCacheDirectory = new File("plugins/" + plugin.getDescription().getName() + "/libs").toPath();
        if (Files.exists(oldCacheDirectory)) {
            try {
                MoreFiles.deleteDirectory(oldCacheDirectory);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Unable to delete lib directory", e);
            }
        }

        return cacheDirectory;
    }
}