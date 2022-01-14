/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rodrigo de Paula Freitas Ramos
 */
public class TesteEncoding {

    static Set<File> arquivosConverter = new HashSet<File>();

    public static void main(String[] args) throws Exception {
        String tipos[] = new String[]{".java"};
        mostrarEncodingDiretorio(new File("INSERIR AQUI O CAMINHO DO PROJETO"), tipos);
        for (File arquivo : arquivosConverter) {  
            System.out.println(arquivo.getAbsolutePath());
   //         transform(arquivo, StandardCharsets.ISO_8859_1.toString(), StandardCharsets.UTF_8.toString());
        }

    }

// A LINHA 35 ESTÁ COMENTADA, VISTO QUE DEVEMOS PRIMEIRO LOCALIZAR O ENCODING DIFERENTE, MAS CASO QUEIRA CORRIGIR, DESCOMENTE-A.

    public static void mostrarEncodingDiretorio(File diretorio, String tipos[]) throws Exception {
        File[] listFiles = diretorio.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory() && !file.getName().equals("build")) {
                    mostrarEncodingDiretorio(file, tipos);
                } else {
                    String name = file.getName().toLowerCase();
                    for (String tipo : tipos) {
                        if (name.endsWith(tipo)) {
                            if (detectarCaractereIncorretoArquivo(file)) {
                                arquivosConverter.add(file);
                                System.out.println(file.getAbsolutePath());

                            }

                            break;
                        }

                    }
                }
            }
        }
    }

    public static boolean detectarCaractereIncorretoArquivo(File arquivo) throws Exception {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8);
        BufferedReader buffRead = new BufferedReader(isr);
        String linha = "";

        boolean caractereIncorreto = false;
        while (true) {
            if (linha != null) {

                char[] toCharArray = linha.toCharArray();
                for (char u : toCharArray) {
                    if (u == 65533) {
                        System.out.println(linha);
                        caractereIncorreto = true;
                        break;
                    }
                }
                if (caractereIncorreto) {
                    break;

                }

            } else {
                break;
            }
            linha = buffRead.readLine();

        }
        buffRead.close();

        return caractereIncorreto;
    }

    public static void transform(File source, String srcEncoding, String tgtEncoding) throws IOException, Exception {
        File target = new File("file.tmp");
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(source), srcEncoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));
            char[] buffer = new char[16384];
            int read;
            while ((read = br.read(buffer)) != -1) {
                bw.write(buffer, 0, read);
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } finally {
                if (bw != null) {
                    bw.close();
                }
            }
        }
        
        Files.copy(target.toPath(), source.toPath(), StandardCopyOption.REPLACE_EXISTING);
        

    }
}
