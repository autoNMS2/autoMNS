package script_reader;

import java.io.InputStream;

public class RunScript {
    public static void main(String[] args) {
        try {
            //run the process
            Process p = Runtime.getRuntime().exec("cmd /c scripts\\first.sh");
            //get the input stream
            InputStream is = p.getInputStream();
            //read script execution result
            int i = 0;
            StringBuffer sb = new StringBuffer();
            while ((i = is.read()) != -1)
                sb.append((char) i);
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

