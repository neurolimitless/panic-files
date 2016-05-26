package hido.panic.file;

public class Structure {

    private String name;
    private String path;
    private byte[] data;

    public Structure() {
    }

    public String toString(){
        return "Path: "+path+"\n Name: "+name+"\n Size: "+data.length/1024+" kb.";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
