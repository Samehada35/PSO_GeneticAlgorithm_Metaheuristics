package utils;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;

public class NumberFormatter extends TextFormatter<String>{
	
	public NumberFormatter() {
		this(t->{
			return t.getText().matches("[0-9]*") ? t : null;
		});
	}

	public NumberFormatter(UnaryOperator<Change> filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

}
