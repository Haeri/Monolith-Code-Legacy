package plugins;

public class BinaryConverter implements Plugin{
    private PluginInfo info;

    public BinaryConverter(){
        info = new PluginInfo();
        info.name = "Binary Converter";
        info.author = "Haeri";
        info.description = "Converte hexadecimal numbers to a binary representation.";
        info.version = 1;
    }

    @Override
    public PluginInfo getInfo() {
        return info;
    }

    @Override
    public ResponseData process(ContextData data) {
        ResponseData res = new ResponseData();
        res.replaceDocument = false;

        if (!data.text.isEmpty()) {
            String math = "";

            math = data.text.replaceAll("'", "");

            String result = "";
            try {
                int mat = Integer.parseInt(math);
                result = Integer.toBinaryString(mat);
            } catch (Exception e) {
                System.out.println("DIdn't work");
            }

            res.response = data.text + " = " + result.toString();
        }

        return res;
    }
}
