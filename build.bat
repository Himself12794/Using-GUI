[ -d bin ] || mkdir bin
javac -sourcepath src/main/java/ -d bin/ src/main/java/com/himself12794/guipractice/ImagePanel.java src/main/java/com/himself12794/guipractice/MainFrame.java
jar -cvfm app.jar MANIFEST.MF -C bin/ .
