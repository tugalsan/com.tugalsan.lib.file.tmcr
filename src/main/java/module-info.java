module com.tugalsan.api.file.tmcr { 
    requires java.desktop;
    requires itextpdf;
    requires com.tugalsan.api.tuple;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.crypto;
    requires com.tugalsan.api.coronator;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.file.docx;
    requires com.tugalsan.api.file.xlsx;
    requires com.tugalsan.api.tomcat;
    requires com.tugalsan.api.file.html;
    requires com.tugalsan.api.file.txt;
    requires com.tugalsan.api.file.pdf;
    requires com.tugalsan.api.file.zip;
    requires com.tugalsan.api.file.img;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.charset;
    requires com.tugalsan.api.thread;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.shape;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.gui;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.cast;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.runnable;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.math;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.servlet.url;
    requires com.tugalsan.api.servlet.gwt;
    requires com.tugalsan.api.sql.conn;
    requires com.tugalsan.api.sql.select;
    requires com.tugalsan.api.sql.restbl;
    requires com.tugalsan.api.sql.resultset;
    requires com.tugalsan.api.sql.col.typed;
    requires com.tugalsan.lib.acsrf;
    requires com.tugalsan.lib.scale;
    requires com.tugalsan.lib.boot;
    requires com.tugalsan.lib.file;
    requires com.tugalsan.lib.table;
    requires com.tugalsan.lib.resource;
    requires com.tugalsan.lib.domain;
    requires com.tugalsan.lib.login;
    requires com.tugalsan.lib.route;
    requires com.tugalsan.lib.rql;
    requires com.tugalsan.lib.rql.txt;
    requires com.tugalsan.lib.rql.link;
    requires com.tugalsan.lib.rql.buffer;
    requires com.tugalsan.lib.rql.allow;
    requires com.tugalsan.lib.rql.report;
    requires com.tugalsan.lib.rql.rev;
    exports com.tugalsan.api.file.tmcr.client;
    exports com.tugalsan.api.file.tmcr.server.file;
    exports com.tugalsan.api.file.tmcr.server.code.filename;
    exports com.tugalsan.api.file.tmcr.server.code.font;
    exports com.tugalsan.api.file.tmcr.server.code.image;
    exports com.tugalsan.api.file.tmcr.server.code.inject;
    exports com.tugalsan.api.file.tmcr.server.code.label;
    exports com.tugalsan.api.file.tmcr.server.code.map;
    exports com.tugalsan.api.file.tmcr.server.code.page;
    exports com.tugalsan.api.file.tmcr.server.code.parser;
    exports com.tugalsan.api.file.tmcr.server.code.table;
    exports com.tugalsan.api.file.tmcr.server.code.text;
    exports com.tugalsan.api.file.tmcr.server.code.url;
}
