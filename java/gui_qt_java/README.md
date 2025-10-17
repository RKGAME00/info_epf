Qt (Qt Jambi) Java scaffold

This folder contains notes and a minimal guide for using Qt with Java (Qt Jambi).

Important:
- Qt Jambi and other Java bindings to Qt are not actively maintained by the Qt Company.
- Using Qt with Java typically requires native libraries and platform-specific packaging.

What I include here:
- a short explanation of options
- placeholder that you can use to adapt a Qt-based UI if you decide to invest in native bindings.

Options:
1) Qt Jambi (older bindings)
   - You will have to download a Qt Jambi distribution or compile bindings for your platform.
   - Example usage is not provided here as it requires native libraries.

2) Two-process approach
   - Keep your Java backend, implement a separate Qt/QML frontend in C++/Python/QtQuick, and communicate over HTTP or WebSocket.
   - This is pragmatic and avoids JNI.

Recommendation:
- Prefer JavaFX for a pure-Java GUI.
- If you must use Qt, implement the frontend separately (in Qt/C++ or Python) and communicate with the Java backend via a small HTTP API.
