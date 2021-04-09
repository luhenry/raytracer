/*
 * Copyright (c) Ludovic Henry. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Ludovic Henry designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Ludovic Henry in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact hi@ludovic.dev or visit ludovic.dev if you need additional
 * information or have any questions.
 */

package dev.ludovic.raytracer.profiler;

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.profile.ProfilerException;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.TextResult;
import org.openjdk.jmh.util.FileUtils;
import org.openjdk.jmh.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public final class VTuneProfiler implements ExternalProfiler {

    protected final String collect;

    public VTuneProfiler() {
        collect = null;
    }

    public VTuneProfiler(String initParams) throws ProfilerException {
        collect = initParams.isEmpty() ? null : initParams;
    }

    @Override
    public Collection<String> addJVMInvokeOptions(BenchmarkParams params) {
        long delay = TimeUnit.NANOSECONDS.toSeconds(params.getWarmup().getCount() *
                params.getWarmup().getTime().convertTo(TimeUnit.NANOSECONDS))
                + TimeUnit.SECONDS.toSeconds(1); // loosely account for the JVM lag
        return Arrays.asList("vtune", "-collect", collect != null ? collect : "uarch-exploration", "-no-summary", "-loop-mode=loop-and-function", "-inline-mode=on", "-resume-after", String.valueOf(delay));
    }

    @Override
    public Collection<String> addJVMOptions(BenchmarkParams params) {
        return Collections.emptyList();
    }

    @Override
    public void beforeTrial(BenchmarkParams params) {}

    @Override
    public Collection<? extends Result> afterTrial(BenchmarkResult br, long pid, File stdOut, File stdErr) {
        return Collections.emptyList();
    }

    @Override
    public boolean allowPrintOut() {
        return false;
    }

    @Override
    public boolean allowPrintErr() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Intel VTune profiler";
    }

}

