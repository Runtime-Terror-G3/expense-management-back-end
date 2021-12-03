package service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraperService {

    public static final String url = "https://altex.ro/laptop-gaming-asus-rog-strix-scar-15-g533qs-hq122-amd-ryzen-9-5900hx-pana-la-4-6ghz-15-6-qhd-32gb-ssd-2tb-nvidia-geforce-rtx-3080-16gb-free-dos-negru/cpd/LAPG533QSHQ122/";
    //public static final String url ="https://altex.ro/microsistem-audio-panasonic-sc-pm250ecs-20w-bluetooth-usb-cd-radio-fm-argintiu/cpd/MICSCPM250ECS/";
    public static String getProductsFromAltex(String url) throws IOException {
        String result = "";

        Document doc = Jsoup.connect(url).get();
        String imageUrl = doc.select("div.slick-slide.slick-active.slick-current").get(0).select("img").attr("src");
        System.out.println("Image url:");
        System.out.println(imageUrl);

        String priceInt = doc.select(".Price-int.leading-none").get(0).text().replace(".","");
        String priceDec = doc.select(".Price-int.leading-none").next().get(0).text().replace(',','.');
        String price = priceInt+priceDec;
        System.out.println("\nPrice:");
        System.out.println(price);

        return result;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getProductsFromAltex(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
