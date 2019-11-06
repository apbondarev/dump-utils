import java.util.Objects;

import sun.jvm.hotspot.code.CodeBlob;
import sun.jvm.hotspot.code.CodeCacheVisitor;
import sun.jvm.hotspot.code.NMethod;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;

/**
 * Dump CodeCache on JDK 8
 * java -cp $JAVA_HOME/lib/sa-jdi.jar:. DumpCodeCache PID
 * https://stackoverflow.com/a/57012008/8058135
 */
public class DumpCodeCache extends Tool {

    @Override
    public void run() {
        VM.getVM().getCodeCache().iterate(new CodeCacheVisitor() {

            public void prologue(Address start, Address end) {
            }

            public void visit(CodeBlob blob) {
                if (blob != null) {
                    if (blob instanceof NMethod) {
                        NMethod method = (NMethod) blob;
                        if (method.getMethod() == null) {
                            System.out.printf("%8d\tnull method\n", method.getSize());
                        } else {
                            System.out.printf("%8d\t%s\n", method.getSize(), method.getName());
                        }
                    } else {
                        System.out.printf("%8d\t%s\n", blob.getSize(), Objects.toString(blob.getName(), "null"));
                    }
                }
            }

            public void epilogue() {
            }
        });
    }

    public static void main(String[] args) {
        new DumpCodeCache().execute(args);
    }
}
