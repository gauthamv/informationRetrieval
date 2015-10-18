import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Slider;



public class SearchEngineIR {

	public static Shell shell;
	public static Text text;
	public static Text OutputBox;
	public static Browser browser;
	public static StringBuilder outputText;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SearchEngineIR window = new SearchEngineIR();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		//shell.setSize(800, 750);
		shell.setBounds(0,0,800,750);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(272, 42, 244, 23);
		//OutputBox = new Text(shell, SWT.BORDER | SWT.H_SCROLL);
		//OutputBox.setBounds(64, 113, 675, 510);
		ToolBar menubar = new ToolBar(shell, SWT.PUSH);
		ToolItem BackButton = new ToolItem(menubar, SWT.PUSH);
		BackButton.setText("Back");
		ToolItem ForwardButton = new ToolItem(menubar,SWT.PUSH);
		ForwardButton.setText("Forward");
		ToolItem StopButton = new ToolItem(menubar, SWT.PUSH);
		StopButton.setText("Stop");
		ToolItem RefreshButton = new ToolItem(menubar, SWT.PUSH);
		RefreshButton.setText("Refresh");
		menubar.setBounds(0, 0,200, 20);
		
	
		
		browser = new Browser(shell, SWT.NONE);
		browser.setBounds(50,120,700,500);
		Button SearchButton = new Button(shell, SWT.NONE);
		SearchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//OutputBox.setText("");
				
			try
			{
				outputText = new StringBuilder();
				  
				String QueryTerm = "";
				String[] Temp = null;
				
				//String QueryTerm = text.getText();
				
				QueryTerm = text.getText();
				System.out.println("Query Term is "+QueryTerm);
				//QueryTerm = combo.getItem(0);
				Temp = QueryTerm.split("\\s");
				System.out.println("Temp is "+Temp[0]);
				for(String temp:Temp)
				{
					SEngine.Query.add(temp);
				}
				System.out.println("SEngine Query is "+SEngine.Query);
				WordFrequencies.InitializeStopWords();  //Stop words Hash is initialized
				//OutputBox.setVisible(false);
				outputText = SEngine.Search();
				//OutputBox.setVisible(false);
				browser.setText(outputText.toString());
				//OutputBox.setText(outputText.toString());
				//OutputBox.setVisible(true);
				//text.setText("");
				SEngine.Query.clear();
				
			}
			catch(Exception e1)
			{
				System.out.println(e1.getMessage());
			}
				
				
			}
		});
		
		Listener listener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem)event.widget;
				String string = item.getText();
				if (string.equals("Back")) 
					if(browser.isBackEnabled())
					 {
						browser.back();
					 }
					else
					{
						browser.setText(outputText.toString());
					}
				else if (string.equals("Forward")) browser.forward();
				else if (string.equals("Stop")) browser.stop();
				else if (string.equals("Refresh")) browser.refresh();
		   }
		};
		
		BackButton.addListener(SWT.Selection, listener);
		ForwardButton.addListener(SWT.Selection, listener);
		StopButton.addListener(SWT.Selection, listener);
		RefreshButton.addListener(SWT.Selection, listener);
		SearchButton.setBounds(340, 79, 95, 28);
		SearchButton.setText("Search");
	}
}