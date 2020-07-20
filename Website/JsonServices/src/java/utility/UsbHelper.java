/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Add By Net
 * @since 3/1/2015
 */
public class UsbHelper {
    private static final String KEY_FILENAME = "Key.txt";
    private static FilenameFilter filterTextFile = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if (name.lastIndexOf('.') > 0) {
                int lastIndex = name.lastIndexOf('.');
                String str = name.substring(lastIndex);
                
                if (str.toLowerCase().equals(".txt")) {
                    return true;
                }
            }
            return false;
        }
    };
    
    public static ArrayList<String> getRemovableDevices() {
        ArrayList<String> removableDevices = new ArrayList<String>();

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] f = File.listRoots();
        for (int i = 0; i < f.length; i++) {
            if (fsv.getSystemDisplayName(f[i]).toLowerCase().contains("removable")) {
                removableDevices.add(f[i].toString());
            }
        }

        return removableDevices;
    }
    
    public static File getFileContainingKey() {
        File retKeyFile = null;

        ArrayList<String> removableDevices = getRemovableDevices();
        int numOfDevices = removableDevices.size();
        for (int i = 0; i < numOfDevices; i++) {
            File f = new File(removableDevices.get(i));
            for (String a : f.list(filterTextFile)) {
                if (a.equals(KEY_FILENAME)) {
                    retKeyFile = new File(f.getPath() + "\\" + a);
                    break;
                }
            }
            if (null != retKeyFile) {
                break;
            }
        }

        return retKeyFile;
    }
    
    public static String getKeyFromUsb() {
        String key = "";
        File f = getFileContainingKey();
        
        try {
            FileReader fr = new FileReader(f);
            FileInputStream fs = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            key = br.readLine();
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        }
        
        return key;
    }
}
