package com.sky.model.menu.almanac;

/**
 * Created by Administrator on 17-9-4.
 * 文件解析
 */
public class AnnotationFactory {

//    public void getAnotationData(Context context){
//        InputStream inputStream = null;
//        try {
//            inputStream = context.getResources().getAssets().open("annotation.xml");
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }finally{
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
    public List<An> getBooks(InputStream stream) {
        List<Book> list = new ArrayList<Book>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 获取XML文档结构
            Document document = builder.parse(stream);
            // 获取根节点
            Element rootElement = document.getDocumentElement();
            NodeList nodeList = rootElement.getElementsByTagName("Book");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Book book = new Book();
                // Node转成Element
                Element element = (Element) nodeList.item(i);
                book.setName(element.getAttribute("name"));
                Element eleTitlElement = (Element) element
                        .getElementsByTagName("Title").item(0);
                String title = eleTitlElement.getFirstChild().getNodeValue();
                Element elePicElement = (Element) element.getElementsByTagName(
                        "Picture").item(0);
                String picString = elePicElement.getFirstChild().getNodeValue();
                book.setTitle(title);
                book.setPicture(picString);
                list.add(book);
            }
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;

    }**/
}
