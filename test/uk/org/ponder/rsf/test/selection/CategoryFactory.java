/*
 * Created on 25 Feb 2008
 */
package uk.org.ponder.rsf.test.selection;


public class CategoryFactory {

  public Category findCategory(int id) {
    String name = Integer.toString(id);
    Category category = new Category();
    category.id = name;
    category.name = "Category " + name;
    return category;
  }

}
