module com.wmy {
    requires transitive javafx.graphics;

    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.wmy to javafx.fxml;

    exports com.wmy;
    exports com.wmy.controller;
    exports com.wmy.models;
    exports com.wmy.models.adt;
    exports com.wmy.models.values;
    exports com.wmy.models.types;
    exports com.wmy.models.statements;
    exports com.wmy.models.expressions;
    exports com.wmy.repo;
}
