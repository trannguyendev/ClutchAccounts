/**
 * Detects if developer tools are open in the browser
 */
export class DevToolsDetector {
  constructor() {
    this.isOpen = false;
    this.listeners = [];
  }

  /**
   * Initialize the detector and start monitoring
   */
  init() {
    this.detectDevTools();
    this.monitorDevTools();
  }

  /**
   * Check if dev tools are currently open using multiple detection methods
   */
  detectDevTools() {
    const threshold = 160; // Minimum height/width for dev tools window

    // Method 1: Check window outer/inner dimensions
    const isDevToolsOpenBySize =
      window.outerHeight - window.innerHeight > threshold ||
      window.outerWidth - window.innerWidth > threshold;

    // Method 2: Check console object
    const isDevToolsOpenByConsole = this.checkConsoleOpen();

    // Method 3: Check debugger statement timing
    const isDevToolsOpenByDebugger = this.checkDebuggerTiming();

    const wasOpen = this.isOpen;
    this.isOpen = isDevToolsOpenBySize || isDevToolsOpenByConsole || isDevToolsOpenByDebugger;

    // Notify listeners if state changed
    if (wasOpen !== this.isOpen) {
      this.notifyListeners(this.isOpen);
    }
  }

  /**
   * Check if console is open by measuring function execution time
   */
  checkConsoleOpen() {
    const start = performance.now();
    // This function is expensive to debug, so if it takes longer than normal, console might be open
    debugger; // eslint-disable-line no-debugger
    const end = performance.now();
    return end - start > 100;
  }

  /**
   * Check debugger timing
   */
  checkDebuggerTiming() {
    const start = performance.now();
    // Empty function to check execution time
    const checkFn = () => {
      // The presence of debugger slows this down significantly
      debugger; // eslint-disable-line no-debugger
    };
    checkFn();
    const end = performance.now();
    return end - start > 100;
  }

  /**
   * Monitor dev tools continuously
   */
  monitorDevTools() {
    // Check every 500ms
    setInterval(() => {
      this.detectDevTools();
    }, 500);

    // Also check on visibility change
    document.addEventListener('visibilitychange', () => {
      this.detectDevTools();
    });

    // Check on resize (when dev tools are toggled, window resizes)
    window.addEventListener('resize', () => {
      this.detectDevTools();
    });
  }

  /**
   * Subscribe to dev tools state changes
   */
  subscribe(callback) {
    this.listeners.push(callback);
    // Immediately call with current state
    callback(this.isOpen);
  }

  /**
   * Notify all listeners of state change
   */
  notifyListeners(isOpen) {
    this.listeners.forEach(listener => {
      try {
        listener(isOpen);
      } catch (error) {
        console.error('Error in dev tools listener:', error);
      }
    });
  }

  /**
   * Get current state
   */
  getState() {
    return this.isOpen;
  }
}

// Export singleton instance
export const devToolsDetector = new DevToolsDetector();
