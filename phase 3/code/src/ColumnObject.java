class ColumnObject extends DataObject {
    int size; // Number of 1s in the column
    String name;

    ColumnObject() {
        super(); // Inherit left,right,up,down,C from dataObject
        this.header = this;
        this.size = 0;
        this.name = "";
    }

    void unlink() {
        this.unlinkFromRow();

        for (DataObject i = this.down; i != this.header; i = i.down) {
            for (DataObject j = i.right; j != i; j = j.right) {
                j.unlinkFromColumn();
            }
        }
    }

    void link() {
        for (DataObject i = this.up; i != this.header; i = i.up) {
            for (DataObject j = i.left; j != i; j = j.left) {
                j.relinkToColumn();
            }
        }

        this.relinkToRow();
    }

}