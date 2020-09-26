package archiver;

/**
 * Получает свойства файла из архива
 */
public class FileProperties {
    private String name;
    private long size;
    private long compressedSize;
    private int compressionMethod;

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public FileProperties(String name, long size, long compressedSize, int compressionMethod) {
        this.name = name;
        this.size = size;
        this.compressedSize = compressedSize;
        this.compressionMethod = compressionMethod;
    }

    /**
     * Считает степень сжатия файла
     * @return
     */
    public long getCompressionRatio() {
        long value = 100 - ((compressedSize * 100) / size);
        return value;
    }

    @Override
    public String toString() {

        if(size > 0)
            return String.format("%s %d Kb (%d Kb) сжатие: %d%%", getName(), getSize() / 1024, getCompressedSize() / 1024, getCompressionRatio());

        else
            return name;
    }
}
