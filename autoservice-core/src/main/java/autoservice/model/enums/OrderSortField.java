package autoservice.model.enums;

public enum OrderSortField {
    PRICE("price"),
    START_TIME("startTime"),
    END_TIME("endTime"),
    CREATED_AT("createdAt");

    private final String fieldName;

    OrderSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
