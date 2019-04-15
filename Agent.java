import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;
// import java.io.BufferedReader;
// import java.io.IOException;

public class Agent{
    public static void main(String[] args) throws IOException{
        System.out.println("test");
        receive_message();
    }

    public static void receive_message() throws IOException{
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 54356));
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
            


            // String next_commands = "";
            // System.out.println(commands);
            // bw.write(9);
            // bw.flush();
            // while (!next_commands.equals("end")) {
            //     next_commands = brReader.readLine();
            //     System.out.println(next_commands);
            //     bw.write(9);
            //     if (next_commands != commands){
            //         System.out.println(next_commands);
            //         // int random_int = get_random_int();
            //         // System.out.println(random_int);
            //         // get_message.println(9);
            //         // get_message.write(9);
            //         // get_message.flush();
            //     }
                
            // }
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public static int get_random_int() {
        Random rd = new Random();
        return rd.nextInt(9) + 1;
    }



}