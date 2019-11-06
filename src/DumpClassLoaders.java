import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;

/**
 * Dump loaded classes with classloaders
 * java -cp $JAVA_HOME/lib/sa-jdi.jar:. DumpClassLoaders PID
 * http://www.javamagazine.mozaicreader.com/JanFeb2017/Default/29/0#&pageSet=32&page=0
 */
public class DumpClassLoaders extends Tool {

    @Override
    public void run() {
        VM.getVM().getSystemDictionary().classesDo(
                (klass, loader) -> {
                    String className = klass.getName().asString();
                    String loaderName = loader == null
                            ? "Bootstrap Classloader"
                            : loader.getKlass().getName().asString();
                    String loaderId = loader == null
                            ? "0"
                            : Long.toHexString(loader.identityHash());
                    System.out.printf("%s\t%s\t%s\n", className, loaderName, loaderId);
                }
        );
    }

    public static void main(String[] args) {
        new DumpClassLoaders().execute(args);
    }
}
