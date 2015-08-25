import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

public class App {
  public static void main(String[] args){
    //staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("tasks", request.session().attribute("tasks"));

      return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());

   get("/categories/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/category-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save();
      model.put("category", newCategory);
      model.put("template", "templates/success.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      // Id parameter is key from the database when we use the save method
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("category", category);
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories/:id/tasks/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("category", category);
      model.put("template", "templates/category-tasks-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.queryParams("categoryId")));
      String description = request.queryParams("description");
      //System.out.println(description);
      Task newTask = new Task(description, category.getId());
      newTask.save();
      model.put("category", category);
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

 }

}
