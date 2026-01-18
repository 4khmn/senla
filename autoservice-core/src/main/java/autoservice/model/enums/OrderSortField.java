package autoservice.model.enums;

public enum OrderSortField {
    PRICE("price"),
    START_TIME("start_time"),
    END_TIME("end_time"),
    CREATED_AT("created_at");

    private final String column;

    OrderSortField(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
