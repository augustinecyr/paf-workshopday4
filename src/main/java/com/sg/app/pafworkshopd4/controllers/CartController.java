package com.sg.app.pafworkshopd4.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sg.app.pafworkshopd4.exception.OrderException;
import com.sg.app.pafworkshopd4.models.LineItem;
import com.sg.app.pafworkshopd4.models.PurchaseOrder;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/cart")
public class CartController {

    @PostMapping
    public String postCart(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession sess)
            throws OrderException {

        List<LineItem> lineItems = (List<LineItem>) sess.getAttribute("cart");
        if (null == lineItems) {
            System.out.println("This is a new session");
            System.out.printf("Session ID = %s\n", sess.getId());
            lineItems = new LinkedList<>();
            sess.setAttribute("cart", lineItems);
        }

        String item = form.getFirst("item");
        Integer quantity = Integer.parseInt(form.getFirst("quantity"));
        lineItems.add(new LineItem(item, quantity));
        PurchaseOrder po = new PurchaseOrder();
        po.setName(form.getFirst("name"));
        for (LineItem li : lineItems)
            System.out.printf("description: %s, quantity: %d\n", li.getDescription(), li.getQuantity());
        po.setLineItems(lineItems);

        sess.setAttribute("checkoutCart", po);
        model.addAttribute("lineItems", lineItems);

        return "cart_template";
    }
}
