package com.example.chat.tcp;

final public class Protocol {

    public static final int LENGTH_OF_HEAD = 4;

    public static final int LENGTH_OF_BIZ = 4;

    public static final int LENGTH_OF_VERSION = 4;

    public static int getLength() {
        return LENGTH_OF_HEAD + LENGTH_OF_BIZ + LENGTH_OF_VERSION;
    }

}
