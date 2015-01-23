/**
 * Copyright 2015 Jamie Mansfield <https://github.com/lexware>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.lexware.unopack.buildtool;

import com.google.gson.Gson;
import io.github.lexware.unopack.buildtool.api.Project;
import io.github.lexware.unopack.buildtool.api.Response;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by jamie on 22/01/15.
 */
public class UnoBuildTool {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File directory = new File(UnoBuildTool.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile();
        File buildDir = new File(directory, "build");
        File build = new File(directory, "build.json");
        Response res = new Gson().fromJson(new FileReader(build), Response.class);
        System.out.println("Starting to build");
        buildDir.mkdirs();
        for(Project project : res.getProjects()) {
            System.out.println("Building " + project.getName() + " " + project.getVersion());
            File projectDir = new File(directory, project.getDir());

            byte[] buffer = new byte[1024];
            FileOutputStream fout = new FileOutputStream(new File(buildDir, project.getName() + "_" + project.getVersion() + ".zip"));
            ZipOutputStream zout = new ZipOutputStream(fout);
            
            for(File file : projectDir.listFiles()) {
                FileInputStream fin = new FileInputStream(file);
                zout.putNextEntry(new ZipEntry(file.getName()));

                int length;
                while((length = fin.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }

                zout.closeEntry();
                fin.close();
            }

            zout.close();
            System.out.println("Finished building " + project.getName() + " " + project.getVersion());
        }
        System.out.println("Finished building");
    }
}
