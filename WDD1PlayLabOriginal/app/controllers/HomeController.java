package controllers;

import play.mvc.*;

import views.html.*;

import play.api.Environment;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import models.*;



/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private FormFactory formFactory;

    @Inject
    public HomeController(FormFactory f){
        this.formFactory=f;
    }
    public Result index() {
        return ok(index.render());
    }
	public Result onsale(Long cat) {
        List<ItemOnSale> itemList = null;
        List<Category> categoryList = Category.findAll();

        if(cat == 0){
            itemList = ItemOnSale.findAll();
        }else {
            itemList = Category.find.ref(cat).getItems();
        }
        return ok(onsale.render(itemList, categoryList));
     }
	public Result about() {
        return ok(about.render());
    }
    public Result additem() {
        Form<ItemOnSale> itemForm = formFactory.form(ItemOnSale.class);
        return ok(additem.render(itemForm));
    }
    public Result addItemSubmit(){
        Form<ItemOnSale> newItemForm = formFactory.form(ItemOnSale.class).bindFromRequest();

        if (newItemForm.hasErrors()){
            return badRequest(additem.render(newItemForm));
        } else{
            ItemOnSale newItem = newItemForm.get();
            if (newItem.getId() == null) {
                newItem.save();
            } else {
                newItem.update();
            }
            flash("success", "Item " + newItem.getName() + " was added/updated ");
            return redirect(controllers.routes.HomeController.onsale(0));
        }
    }
    public Result deleteItem(Long id) {
        ItemOnSale.find.ref(id).delete();
        flash("success", "Item has been deleted.");
        return redirect(controllers.routes.HomeController.onsale(0));
    }
    public Result updateItem(Long id) {
        ItemOnSale i;
        Form<ItemOnSale> itemForm;

        try {
            i = ItemOnSale.find.byId(id);
            itemForm = formFactory.form(ItemOnSale.class).fill(i);
        } catch (Exception ex) {
            return badRequest("error");
        }
        return ok(additem.render(itemForm));
        }
    }

        