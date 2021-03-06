package lslforge.debug;

import lslforge.LSLForgePlugin;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;

public class LSLDebugTarget implements IDebugTarget, IProcessListener {
    public static final String LSLFORGE = "lslforge"; //$NON-NLS-1$
    private String name;
    private LSLProcess process;
    private LSLThread thread;
    private IThread[] threads;
    private ILaunch launch;
    private boolean suspended = false;
    private boolean terminated;
    public LSLDebugTarget(String name, ILaunch launch, LSLProcess process) {
        this.name = name;
        this.process = process;
        this.launch = launch;
        thread = new LSLThread(this);
        threads = new LSLThread[] { thread };
        process.setThread(thread);
        process.addListener(this);
    }

    public String getName() throws DebugException {
        return name;
    }

    public IProcess getProcess() {
        return process;
    }

    public IThread[] getThreads() throws DebugException {
        return threads;
    }

    public boolean hasThreads() throws DebugException {
        return true;
    }

    public boolean supportsBreakpoint(IBreakpoint breakpoint) {
        return (breakpoint instanceof ILineBreakpoint);
    }

    public String getModelIdentifier() {
        return LSLFORGE;
    }

    public boolean canTerminate() {
        return false;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated() {
        this.terminated = true;
        DebugPlugin.getDefault().fireDebugEventSet(
                new DebugEvent[] {
                        new DebugEvent(this, DebugEvent.TERMINATE)
                });
    }
    
    public void terminate() throws DebugException {
    }

    public boolean canResume() {
        return suspended;
    }

    public boolean canSuspend() {
        return false;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void resume() throws DebugException {
    }

    public void suspend() throws DebugException { }
    void setSuspended() {
        suspended = true;
        DebugPlugin.getDefault().fireDebugEventSet(
            new DebugEvent[] {
                    new DebugEvent(this, DebugEvent.SUSPEND, DebugEvent.BREAKPOINT)
            });
    }

    public void breakpointAdded(IBreakpoint breakpoint) {
        // TODO Auto-generated method stub

    }

    public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
        // TODO Auto-generated method stub

    }

    public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
        // TODO Auto-generated method stub

    }

    public boolean canDisconnect() {
        return false;
    }

    public void disconnect() throws DebugException {
        throw notSupported();
    }

    private DebugException notSupported() throws DebugException {
        return new DebugException(
                new Status(IStatus.ERROR,LSLForgePlugin.PLUGIN_ID,
                        DebugException.NOT_SUPPORTED,"",null)); //$NON-NLS-1$
    }

    public boolean isDisconnected() {
        return false;
    }

    public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
        throw notSupported();
    }

    public boolean supportsStorageRetrieval() {
        return false;
    }

    public IDebugTarget getDebugTarget() {
        return this;
    }

    public ILaunch getLaunch() {
        return launch;
    }

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {

        if (ILaunch.class.equals(adapter)) {
            return getLaunch();
        }
        return null;
    }

    public void processTerminated(LSLProcess p) {
        setTerminated();
        p.removeListener(this);
    }

}
