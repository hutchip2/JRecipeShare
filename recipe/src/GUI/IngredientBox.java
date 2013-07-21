package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Logic.Connectdatabase;
import Logic.Ingredient;
import Logic.Recipe;

import net.miginfocom.swing.MigLayout;


public class IngredientBox implements ActionListener {

	ArrayList<Ingredient> ingredients;
	Recipe recipe;
	private JButton addField;
	JComboBox comboName;
	JTextField measureValue;
	JTextField measureType;
	JTextField nutritionValue;
	
	JPanel box = new JPanel(new MigLayout());
	
	public IngredientBox(){
		ingredients = new ArrayList<Ingredient>();
		box = this.getIngredientBox();

	}
	
	public IngredientBox (Recipe recipe) {
		this();
		this.recipe = recipe;
	}
	
	public void addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);
	}
	
	private JPanel getIngredientBox() {
		
		JLabel ingreds 			= new JLabel("Ingrediants: ");
		JLabel name 			= new JLabel("Name");
		JLabel measure 			= new JLabel("Measure");
		JLabel type 			= new JLabel("Type");
		JLabel nutrition 		= new JLabel("Nutrition Value");
		addField				= new JButton("+");
		addField.addActionListener(this);
		
		name.setToolTipText("Ingredient name, ie. Flour ...");
		measure.setToolTipText("Quantity of ingredient");
		type.setToolTipText("Grams, Cups, ect...");
		nutrition.setToolTipText("Caloies in this ingredient");
		
		comboName		= new JComboBox(this.getIngredientNames());
		measureValue   	= new JTextField();
		measureType 	= new JTextField();
		nutritionValue 	= new JTextField();
		
		comboName.setPreferredSize(new Dimension(100, 50));
		measureValue.setPreferredSize(new Dimension(100, 50));
		measureType.setPreferredSize(new Dimension(100, 50));
		nutritionValue.setPreferredSize(new Dimension(100, 50));


		
		box.add(ingreds, 		"cell 0 0, wrap, span, left");
		box.add(name,			"cell 0 1");
		box.add(measure, 		"cell 1 1");
		box.add(type, 			"cell 2 1");
		box.add(nutrition, 		"cell 3 1, wrap");
		
		for (Ingredient ingred : ingredients) {
			
			JTextField dummie = new JTextField();
			dummie.setEditable(false);
			dummie.setBorder(null);
			dummie.setOpaque(false);			
			dummie.setText(ingred.name);
			box.add(dummie, "width 100");
			
			JTextField yummie = new JTextField();
			yummie.setEditable(false);
			yummie.setBorder(null);
			yummie.setOpaque(false);			
			yummie.setText(Double.toString(ingred.measure));
			box.add(yummie, "width 100");
			
			
			JTextField sunny = new JTextField();
			sunny.setEditable(false);
			sunny.setBorder(null);
			sunny.setOpaque(false);
			sunny.setText(ingred.type);
			box.add(sunny, "width 100");
			
			JTextField funny = new JTextField();
			funny.setEditable(false);
			funny.setBorder(null);
			funny.setOpaque(false);
			funny.setText(Double.toString(ingred.nutritionValue));
			box.add(funny, "width 100, wrap");
			
		}
		
		
		box.add(comboName,	 	"width 100");
		box.add(measureValue, 	"width 100");
		box.add(measureType, 	"width 100");
		box.add(nutritionValue, "width 100");
		box.add(addField, 		"wrap");
		
		return box;
	}
	
	
	/**
	 * Retrieves all the available ingredients from the database
	 * @return String array
	 */
	private String[] getIngredientNames() {
		
		//HashMap<String, Image> dbingredients = new HashMap<String, Image>();
		
		ArrayList<String> names = new ArrayList<String>();
		
		Connectdatabase connectdb = new Connectdatabase();
		
		String sql = "SELECT * FROM ingredient";
		
		if (connectdb.connect != null) {
			
			ResultSet result = connectdb.executeselect(sql);
			
			try {
				
				while ( result.next() ) {					
					
					//InputStream is = result.getBinaryStream("ingredientPic");	
					
					//Image thisPic = ImageIO.read(is);		
					
					names.add(result.getString("name"));			
	
				}				
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
	    String[] strResult = new String[names.size()];  
		
		return names.toArray(strResult);
		
	}

	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == addField) {
			
			String name = (String)comboName.getSelectedItem();
			Double measureVal;
			try {
				measureVal = Double.parseDouble(measureValue.getText().trim());
			} catch (NumberFormatException e1) {
				measureVal = 0.0;
			}
			String type = measureType.getText().trim();
			Double nutritionVal;
			try {
				nutritionVal = Double.parseDouble(nutritionValue.getText().trim());
			} catch (NumberFormatException e2) {
				nutritionVal = 0.0;
			}			
			
			if (name != "" && name != null) {
				ingredients.add(new Ingredient(
						name,
						measureVal,
						type,
						nutritionVal
						));
			}
			
		}
		
		box.removeAll();
		box.revalidate();
		box = this.getIngredientBox();
		box.revalidate();
		
		
	}
	
	
	
}
