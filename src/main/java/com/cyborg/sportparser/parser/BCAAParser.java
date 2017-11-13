package com.cyborg.sportparser.parser;


import com.cyborg.sportparser.model.*;
import com.cyborg.sportparser.repository.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BCAAParser {

    private static final String BASE_URL = "https://bcaa.ua";

    private final ProductRepository productRepository;

    @Autowired
    public BCAAParser(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void parserTest() throws IOException {

       /* Document document = Jsoup.connect("https://bcaa.ua/proteini/muscle_tech_100_whey_plus_907_gramm#assortment_shokolad").get();

        productRepository.deleteAll();

        Product product = new Product(getProductName(document), getProductDescription(document), getProductImage(document));
        System.out.println(product.toString());
        productRepository.save(product);*/

       productRepository.deleteAll();

        List<String> allCategoryLinks = new ArrayList<>();
        allCategoryLinks.add("https://bcaa.ua/proteini");


        List<String> allProductLinks = getAllProductLinks(allCategoryLinks).stream().collect(Collectors.toList());
        System.out.println(" FINISHED " + " all products count = " + allProductLinks.size());

        parseAndSave(allProductLinks);

    }

    private void parseAndSave(List<String> productLinks) {

        System.out.println("-=Parsing each product=-");
        System.out.println("all products = " + productLinks.size());

        for (int i = 0; i < productLinks.size(); i++) {

            System.out.println("N = " + i);

            Document document = null;
            try {
                document = Jsoup.connect(productLinks.get(i)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (document !=null) {
                Product product = new Product(getProductName(document), getProductDescription(document), getProductImage(document));
                productRepository.save(product);
                System.out.println("saving N = " + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("DONE");
    }

    private static Set<String> getAllProductLinks(List<String> allCategoryLinks) throws IOException {
        Set<String> allProductLinks = new HashSet<>();

        System.out.println(" start parsing all product links " + " categories count = " + allCategoryLinks.size());

        for (String categoryLink : allCategoryLinks) {
            Document productsFromPage = getDocumentByLink(categoryLink);
            for (int i = 1; i <= getLastPage(productsFromPage); i++) {

                System.out.println(" category = " + categoryLink + " /  page = " + i);

                productsFromPage = getDocumentByLink(categoryLink + "/" + i);

                Set<String> linkToProducts = getProductLinks(productsFromPage);
                allProductLinks.addAll(linkToProducts);

            }
        }

        return allProductLinks;
    }

    private static Set<String> getProductLinks(Document document) {
        return document
                .select("div.product-table")
                .select("div.product-block")
                .select("div.title")
                .select("a")
                .stream()
                .map(element -> BASE_URL + element.attr("href"))
                .collect(Collectors.toSet());
    }

    private static int getLastPage(Document document) {
        int page = 1;
        try {
            String lastPage = document.select("div.pagination").select("li.page").last().select("a").html();
            page = Integer.valueOf(lastPage);
        } catch (Exception e) {}

        return page;
    }

    private static Document getDocumentByLink(String link) {
        try {
            Thread.sleep(2000);
            return Jsoup.connect(link).get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String getProductName(Document document) {
        return document
                .select("div.description-product")
                .select("div.info")
                .select("div.title")
                .select("div.main-title")
                .select("h1")
                .html();
    }

    private String getProductBrand(Document document) {
        return BASE_URL + document
                .select("div.image")
                .select("img")
                .attr("src");
    }

    private String getProductImage(Document document) {
        return BASE_URL + document
                .select("div.image")
                .select("img")
                .attr("src");
    }

    private Description getProductDescription(Document document) {

        String header = String.valueOf(document.select("div.product-content").select("h4").html());

        Elements elementsDescription = document.select("div.product-content").select("p");
        List<String> descriptions = elementsDescription.stream().map(Element::html).collect(Collectors.toList());

        for (int i = 0; i < descriptions.size(); i++) {
            if (descriptions.get(i).startsWith("<strong")) {
                String s = descriptions.get(i).replace("<strong>", "");
                s = s.replace("</strong>", "");
                s = s.replace("&nbsp;","");
                descriptions.set(i, s);
            }
        }


        String compositionName = String.valueOf(document.select("div.product-content").select("h2").html());
        Elements elementsComposition = document.select("div.product-content").select("table").select("tbody").select("tr").select("td");
        List<String> elementList = elementsComposition.stream().map(Element::html).collect(Collectors.toList());

        Set<Parameter> parameters = new HashSet<>();
        for (int i = 0; i < (elementList.size()-2); i += 2) {
            if (elementList.get(i+1).startsWith("<a href=")) {
                String string = elementList.get(i+1).replaceAll(".*\">", "");
                string = string.replace("</a>","");
                parameters.add(new Parameter(elementList.get(i), string));
            } else
                parameters.add(new Parameter(elementList.get(i), elementList.get(i + 1)));
        }

        List<Parameter> headerParam = getProductParameters(document).stream().collect(Collectors.toList());
        headerParam.forEach(parameters::remove);


        String headerName = String.valueOf(document.select("div.options").select("div.heading").select("h2").html()) + " ";
        headerName += String.valueOf(document.select("div.options").select("div.heading").select("p").html());


        return new Description(new Header(headerName, headerParam), header, descriptions, new Composition(compositionName, parameters.stream().collect(Collectors.toList())));
    }

    private static Set<Parameter> getProductParameters(Document document) {
        Elements elements = document.select("table.parameters").select("tr").select("td");
        List<String> elementList = elements.stream().map(Element::html).collect(Collectors.toList());

        Set<Parameter> parameters = new HashSet<>();
        for (int i = 0; i < elementList.size(); i += 2) {
            if (elementList.get(i+1).startsWith("<a href=")) {
                String string = elementList.get(i+1).replaceAll(".*\">", "");
                string = string.replace("</a>","");
                parameters.add(new Parameter(elementList.get(i), string));
            } else
                parameters.add(new Parameter(elementList.get(i), elementList.get(i + 1)));
        }

        return parameters;
    }
}
