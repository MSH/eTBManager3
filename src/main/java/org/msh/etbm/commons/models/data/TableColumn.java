package org.msh.etbm.commons.models.data;

/**
 * Store information about a table, used by {@link Field}
 * to support model update.
 * <p>
 *     Information about <b>not null</b> field is not supported by now (controlled by software)
 * </p>
 * Created by rmemoria on 2/1/17.
 */
public class TableColumn {

    /**
     * The field name
     */
    private String name;
    private TableColumnType type;
    private int size;

    public TableColumn(String name, TableColumnType type, int size) {
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public TableColumn(String name, TableColumnType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", size=" + size +
                '}';
    }

    /**
     *
     * @param col
     * @return
     */
    public boolean equalsTo(TableColumn col) {
        return type == col.getType() && size == col.getSize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableColumnType getType() {
        return type;
    }

    public void setType(TableColumnType type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
