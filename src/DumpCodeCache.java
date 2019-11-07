import java.util.Objects;

import sun.jvm.hotspot.code.CodeBlob;
import sun.jvm.hotspot.code.CodeCacheVisitor;
import sun.jvm.hotspot.code.NMethod;
import sun.jvm.hotspot.debugger.Address;
import sun.jvm.hotspot.oops.Method;
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
                        Method internalMethod = method.getMethod();
                        if (internalMethod == null) {
                            System.out.printf("%8d\tnull method\n", method.getSize());
                        } else {
                            long invocationCount = 0L;
                            long backedgeCount = 0L;
                            int interpreterInvocationCount = 0;
                            int interpreterThrowoutCount = 0;
                            if (internalMethod.getMethodCounters() != null) {
                                invocationCount = internalMethod.getInvocationCount();
                                backedgeCount = internalMethod.getBackedgeCount();
                                interpreterInvocationCount = internalMethod.interpreterInvocationCount();
                                interpreterThrowoutCount = internalMethod.interpreterThrowoutCount();
                            }
                            System.out.printf("%8d\t%s\t%s\t%s\t%s\t%s\t%s\n", method.getSize(), method.getName(), invocationCount, backedgeCount, interpreterInvocationCount, interpreterThrowoutCount, method.isOSRMethod());
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
