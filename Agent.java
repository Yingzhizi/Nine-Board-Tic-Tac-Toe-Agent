import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;
// import java.io.BufferedReader;
// import java.io.IOException;

public class Agent{

    public static int default_port = 54321;
    public static String agent_type = "o";

    public static int args_parser(String[] args){
        int i = 0;
        while (i < args.length){
            if (args[i].equals("-x") || args[i].equals("-X")){
                agent_type = "x";
                i += 1;
            } else if (args[i].equals("-o") || args[i].equals("-O")){
                agent_type = "o";
                i += 1;
            } else if (args[i].equals("-p") || args[i].equals("-P")){
                try {
                    int port = Integer.valueOf(args[i+1]);
                    default_port = port;
                } catch (Exception e) {
                }
                i += 2;
            }
        }
        return Agent.default_port;
    }
    public static void main(String[] args) throws IOException{
        System.out.println("test");
        for (int i = 0; i <args.length; i++){
            System.out.println("args["+i+"]="+args[i]);
        }
        int port = args_parser(args);
        System.out.println(port);
        receive_message(port);
    }

    public static void receive_message(int port) throws IOException{
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", port));
            BufferedReader brReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream get_message = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(get_message));
            

            String commands = brReader.readLine();
            System.out.println(commands.length());
            for (int i = 0; i < commands.length(); i++){
                
            }
            while(true){
                System.out.println(commands);
                commands = brReader.readLine();
                if (commands.equals("init.")){

                }
                else if(commands.equals("start(o).")){
                    
                }
                else if(commands.startsWith("second")){
                    bw.write(4);
                    System.out.println("write " + 4 + " to the server");
                    bw.flush();
                    bw.write(1);
                }
            }
            
            
        } catch (Exception e) {
            //TODO: handle exception
        } finally {
            //socket.close();
        }
    }


    public static int get_random_int() {
        Random rd = new Random();
        return rd.nextInt(9) + 1;
    }



}