package org.pentaho.di.ui.trans.steps.sequoiadboutput;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.sequoiadboutput.SequoiaDBOutputField;
import org.pentaho.di.trans.steps.sequoiadboutput.SequoiaDBOutputMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.ColumnInfo;
import org.pentaho.di.ui.core.widget.TableView;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class SequoiaDBOutputDialog extends BaseStepDialog implements StepDialogInterface {

   private static Class<?> PKG = SequoiaDBOutputMeta.class;

   private CTabFolder m_wTabFolder;
   private CTabItem m_wSdbConnectionTab;
   private CTabItem m_wSdbOutputTab;
   private CTabItem m_wSdbFieldsTab;

   private TextVar m_wHostname;
   private TextVar m_wPort;
   private TextVar m_wCSName;
   private TextVar m_wCLName;
   private TableView m_fieldsView;
   private Button m_getFieldsBut;

   private SequoiaDBOutputMeta m_meta;

   public SequoiaDBOutputDialog(Shell parent, Object in,
         TransMeta transMeta, String stepname) {
      super(parent, (BaseStepMeta)in, transMeta, stepname);
      m_meta = (SequoiaDBOutputMeta)in;
   }

   @Override
   public String open() {

      // store some convenient SWT variables 
      Shell parent = getParent();
      Display display = parent.getDisplay();

      // SWT code for preparing the dialog
      shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
      props.setLook(shell);
      setShellImage(shell, m_meta);
      
      // Save the value of the changed flag on the meta object. If the user cancels
      // the dialog, it will be restored to this saved value.
      // The "changed" variable is inherited from BaseStepDialog
      changed = m_meta.hasChanged();
      
      // The ModifyListener used on all controls. It will update the meta object to 
      // indicate that changes are being made.
      ModifyListener lsMod = new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            m_meta.setChanged();
         }
      };
      
      // ------------------------------------------------------- //
      // SWT code for building the actual settings dialog        //
      // ------------------------------------------------------- //
      FormLayout formLayout = new FormLayout();
      formLayout.marginWidth = Const.FORM_MARGIN;
      formLayout.marginHeight = Const.FORM_MARGIN;

      shell.setLayout(formLayout);
      shell.setText(BaseMessages.getString(PKG, "SequoiaDBOutput.Shell.Title")); 

      int middle = props.getMiddlePct();
      int margin = Const.MARGIN;

      // Stepname line
      wlStepname = new Label(shell, SWT.RIGHT);
      wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName")); 
      props.setLook(wlStepname);
      fdlStepname = new FormData();
      fdlStepname.left = new FormAttachment(0, 0);
      fdlStepname.right = new FormAttachment(middle, -margin);
      fdlStepname.top = new FormAttachment(0, margin);
      wlStepname.setLayoutData(fdlStepname);
      
      wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
      wStepname.setText(stepname);
      props.setLook(wStepname);
      wStepname.addModifyListener(lsMod);
      fdStepname = new FormData();
      fdStepname.left = new FormAttachment(middle, 0);
      fdStepname.top = new FormAttachment(0, margin);
      fdStepname.right = new FormAttachment(100, 0);
      wStepname.setLayoutData(fdStepname);
      Control lastControl = wStepname;

      m_wTabFolder = new CTabFolder(shell, SWT.BORDER);
      props.setLook(m_wTabFolder, Props.WIDGET_STYLE_TAB);
      m_wTabFolder.setSimple(false);

      // *************Connection tab***********
      m_wSdbConnectionTab = new CTabItem(m_wTabFolder, SWT.NONE);
      m_wSdbConnectionTab.setText(BaseMessages.getString(PKG,
            "SequoiaDBOutput.ConnectionTab.Title")) ;
      Composite wConnComp = new Composite(m_wTabFolder, SWT.NONE);
      props.setLook(wConnComp);
      FormLayout connLayout = new FormLayout();
      connLayout.marginWidth = 3;
      connLayout.marginHeight = 3;
      wConnComp.setLayout(connLayout);
      
      // Hostname
      Label wlHostname = new Label(wConnComp, SWT.RIGHT);
      wlHostname.setText(BaseMessages.getString(PKG,
            "SequoiaDBOutput.Hostname.Label"));
      props.setLook(wlHostname);
      FormData fdlHostname = new FormData();
      fdlHostname.left = new FormAttachment(0, 0);
      fdlHostname.right = new FormAttachment(middle, -margin);
      fdlHostname.top = new FormAttachment(0, margin);
      wlHostname.setLayoutData(fdlHostname);
      m_wHostname = new TextVar(transMeta, wConnComp, SWT.SINGLE | SWT.LEFT
            | SWT.BORDER);
      props.setLook(m_wHostname);
      m_wHostname.addModifyListener(lsMod);
      FormData fdHostname = new FormData();
      fdHostname.left = new FormAttachment(middle, 0);
      fdHostname.top = new FormAttachment(0, margin);
      fdHostname.right = new FormAttachment(100, 0);
      m_wHostname.setLayoutData(fdHostname);
      lastControl = m_wHostname;

      // Port
      Label wlPort = new Label(wConnComp, SWT.RIGHT);
      wlPort.setText(BaseMessages.getString(PKG, "SequoiaDBOutput.Port.Label"));
      props.setLook(wlPort);
      FormData fdlPort = new FormData();
      fdlPort.left = new FormAttachment(0, 0);
      fdlPort.right = new FormAttachment(middle, -margin);
      fdlPort.top = new FormAttachment(lastControl, margin);
      wlPort.setLayoutData(fdlPort);
      m_wPort = new TextVar(transMeta, wConnComp, SWT.SINGLE | SWT.LEFT
            | SWT.BORDER);
      props.setLook(m_wPort);
      m_wPort.addModifyListener(lsMod);
      FormData fdPort = new FormData();
      fdPort.left = new FormAttachment(middle, 0);
      fdPort.top = new FormAttachment(lastControl, margin);
      fdPort.right = new FormAttachment(100, 0);
      m_wPort.setLayoutData(fdPort);
      lastControl = m_wPort;
      wConnComp.setLayoutData(fdPort);

      wConnComp.layout();
      m_wSdbConnectionTab.setControl(wConnComp);

      // *************Output tab***********
      m_wSdbOutputTab = new CTabItem(m_wTabFolder, SWT.NONE);
      m_wSdbOutputTab.setText(BaseMessages.getString(PKG,
            "SequoiaDBOutput.OutputTab.Title")) ;
      Composite wOutputComp = new Composite(m_wTabFolder, SWT.NONE);
      props.setLook(wOutputComp);
      FormLayout outputLayout = new FormLayout();
      outputLayout.marginWidth = 3;
      outputLayout.marginHeight = 3;
      wOutputComp.setLayout(outputLayout);

      // CSName
      Label wlCSName = new Label(wOutputComp, SWT.RIGHT);
      wlCSName.setText(BaseMessages.getString(PKG,
            "SequoiaDBOutput.CSName.Label"));
      props.setLook(wlCSName);
      FormData fdlCSName = new FormData();
      fdlCSName.left = new FormAttachment(0, 0);
      fdlCSName.right = new FormAttachment(middle, -margin);
      fdlCSName.top = new FormAttachment(0, margin);
      wlCSName.setLayoutData(fdlCSName);
      m_wCSName = new TextVar(transMeta, wOutputComp, SWT.SINGLE | SWT.LEFT
            | SWT.BORDER);
      props.setLook(m_wCSName);
      m_wCSName.addModifyListener(lsMod);
      FormData fdCSName = new FormData();
      fdCSName.left = new FormAttachment(middle, 0);
      fdCSName.top = new FormAttachment(0, margin);
      fdCSName.right = new FormAttachment(100, 0);
      m_wCSName.setLayoutData(fdCSName);
      lastControl = m_wCSName;
      
      // CLName
      Label wlCLName = new Label(wOutputComp, SWT.RIGHT);
      wlCLName.setText(BaseMessages.getString(PKG, "SequoiaDBOutput.CLName.Label"));
      props.setLook(wlCLName);
      FormData fdlCLName = new FormData();
      fdlCLName.left = new FormAttachment(0, 0);
      fdlCLName.right = new FormAttachment(middle, -margin);
      fdlCLName.top = new FormAttachment(lastControl, margin);
      wlCLName.setLayoutData(fdlCLName);
      m_wCLName = new TextVar(transMeta, wOutputComp, SWT.SINGLE | SWT.LEFT
            | SWT.BORDER);
      props.setLook(m_wCLName);
      m_wCLName.addModifyListener(lsMod);
      FormData fdCLName = new FormData();
      fdCLName.left = new FormAttachment(middle, 0);
      fdCLName.top = new FormAttachment(lastControl, margin);
      fdCLName.right = new FormAttachment(100, 0);
      m_wCLName.setLayoutData(fdCLName);
      lastControl = m_wCLName;
      wOutputComp.setLayoutData(fdCLName);
      
      wOutputComp.layout();
      m_wSdbOutputTab.setControl(wOutputComp);

      // *************Fields tab***********
      m_wSdbFieldsTab = new CTabItem(m_wTabFolder, SWT.NONE);
      m_wSdbFieldsTab.setText(BaseMessages.getString(PKG,
            "SequoiaDBOutput.FieldsTab.Title"));
      Composite wFieldsComp = new Composite(m_wTabFolder, SWT.NONE);
      props.setLook(wFieldsComp);
      FormLayout fieldsLayout = new FormLayout();
      fieldsLayout.marginWidth = 3;
      fieldsLayout.marginHeight = 3;
      wFieldsComp.setLayout(fieldsLayout);

      final ColumnInfo[] colinf = new ColumnInfo[] {
          new ColumnInfo(BaseMessages.getString(PKG,
              "SequoiaDBOutput.FieldsTab.FIELD_Name"), //$NON-NLS-1$
              ColumnInfo.COLUMN_TYPE_TEXT, false),
          new ColumnInfo(BaseMessages.getString(PKG,
              "SequoiaDBOutput.FieldsTab.FIELD_Path"), //$NON-NLS-1$
              ColumnInfo.COLUMN_TYPE_TEXT, false), };
      
      // get fields button
      m_getFieldsBut = new Button( wFieldsComp, SWT.PUSH | SWT.CENTER );
      props.setLook( m_getFieldsBut );
      m_getFieldsBut.setText( BaseMessages.getString( PKG,
                                                "SequoiaDBOutput.FieldsTab.Button.Label" ) );
      FormData fdGetFieldsBut = new FormData();
      fdGetFieldsBut.bottom = new FormAttachment( 100, -margin * 2 );
      fdGetFieldsBut.left = new FormAttachment( 0, margin );
      m_getFieldsBut.setLayoutData( fdGetFieldsBut );
      m_getFieldsBut.addSelectionListener( new SelectionAdapter(){
         @Override
         public void widgetSelected( SelectionEvent e ){
            getFields();
         }
      });

      m_fieldsView = new TableView(transMeta, wFieldsComp, SWT.FULL_SELECTION
            | SWT.MULTI, colinf, 1, lsMod, props);
      FormData fdlFields = new FormData();
      fdlFields.top = new FormAttachment(0, margin * 2);
      fdlFields.bottom = new FormAttachment(m_getFieldsBut, -margin * 2);
      fdlFields.left = new FormAttachment(0, 0);
      fdlFields.right = new FormAttachment(100, 0);
      m_fieldsView.setLayoutData(fdlFields);
      
      FormData fdFields = new FormData();
      fdFields.left = new FormAttachment(0, 0);
      fdFields.top = new FormAttachment(0, 0);
      fdFields.right = new FormAttachment(100, 0);
      fdFields.bottom = new FormAttachment(100, 0);
      wFieldsComp.setLayoutData(fdFields);
      
      wFieldsComp.layout();
      m_wSdbFieldsTab.setControl(wFieldsComp);
      

      // configure the tab-folder
      FormData fdTmp = new FormData();
      fdTmp.left = new FormAttachment(0, 0);
      fdTmp.top = new FormAttachment(wStepname, margin);
      fdTmp.right = new FormAttachment(100, 0);
      fdTmp.bottom = new FormAttachment(100, -50);
      m_wTabFolder.setLayoutData(fdTmp);

      // OK and cancel buttons
      wOK = new Button(shell, SWT.PUSH);
      wOK.setText(BaseMessages.getString(PKG, "System.Button.OK")); 
      wCancel = new Button(shell, SWT.PUSH);
      wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel")); 

      BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, m_wTabFolder);
      //setButtonPositions(new Button[] { wOK, wCancel }, margin, m_wTabFolder);

      // Add listeners for cancel and OK
      lsCancel = new Listener() {
         @Override
         public void handleEvent(Event e) {btnCancel();}

      };
      lsOK = new Listener() {
         public void handleEvent(Event e) {btnOk();}
      };

      wCancel.addListener(SWT.Selection, lsCancel);
      wOK.addListener(SWT.Selection, lsOK);

      // default listener (for hitting "enter")
      lsDef = new SelectionAdapter() {
         public void widgetDefaultSelected(SelectionEvent e) {btnOk();}
      };
      wStepname.addSelectionListener(lsDef);
      m_wPort.addSelectionListener(lsDef);

      // Detect X or ALT-F4 or something that kills this window and cancel the dialog properly
      shell.addShellListener(new ShellAdapter() {
         public void shellClosed(ShellEvent e) {btnCancel();}
      });

      m_wTabFolder.setSelection(0);

      // Set/Restore the dialog size based on last position on screen
      // The setSize() method is inherited from BaseStepDialog
      setSize();

      // populate the dialog with the values from the meta object
      populateDialog();
      
      // restore the changed flag to original value, as the modify listeners fire during dialog population 
      m_meta.setChanged(changed);

      // open dialog and enter event loop 
      shell.open();
      while (!shell.isDisposed()) {
         if (!display.readAndDispatch())
            display.sleep();
      }

      // at this point the dialog has closed, so either btnOk() or btnCancel() have been executed
      // The "stepname" variable is inherited from BaseStepDialog
      return stepname;
   }
   
   /**
    * Called when the user cancels the dialog.  
    */
   private void btnCancel() {
      // The "stepname" variable will be the return value for the open() method. 
      // Setting to null to indicate that dialog was cancelled.
      stepname = null;
      // Restoring original "changed" flag on the met aobject
      m_meta.setChanged(changed);
      // close the SWT dialog window
      dispose();
   }
   
   /**
    * Called when the user confirms the dialog
    */
   private void btnOk() {
      // The "stepname" variable will be the return value for the open() method. 
      // Setting to step name from the dialog control
      if (Const.isEmpty(wStepname.getText()))
         return;
      stepname = wStepname.getText(); 

      getConfigure();

      // close the SWT dialog window
      dispose();
   }
   
   private void getFields(){
      try {
         RowMetaInterface rowMeta = transMeta.getPrevStepFields( stepname );
         if ( rowMeta != null ) {
            BaseStepDialog.getFieldsFromPrevious( rowMeta, m_fieldsView, 1,
                                                  new int[]{1}, null, -1, -1,
                                                  null );
         }
      }
      catch ( KettleException e ){
         logError( BaseMessages.getString( PKG, "System.Dialog.GetFieldsFailed.Message" ), //$NON-NLS-1$
               e );
         new ErrorDialog( shell,
               BaseMessages.getString( PKG, "System.Dialog.GetFieldsFailed.Title" ), BaseMessages.getString( PKG, //$NON-NLS-1$
                   "System.Dialog.GetFieldsFailed.Message" ), e ); //$NON-NLS-1$
      }
   }

   /**
    * This helper method puts the step configuration stored in the meta object
    * and puts it into the dialog controls.
    */
   private void populateDialog() {
      m_wHostname.setText(Const.NVL(m_meta.getHostname(), ""));
      m_wPort.setText(Const.NVL(m_meta.getPort(), ""));
      m_wCSName.setText(Const.NVL(m_meta.getCSName(), ""));
      m_wCLName.setText(Const.NVL(m_meta.getCLName(), ""));
      wStepname.selectAll();
      setSelectedFields(m_meta.getSelectedFields());
   }
   
   private void getConfigure()
   {
      m_meta.setHostname(m_wHostname.getText());
      m_meta.setPort(m_wPort.getText());
      m_meta.setCSName(m_wCSName.getText());
      m_meta.setCLName(m_wCLName.getText());
      
      int numFields = m_fieldsView.nrNonEmpty();
      if (numFields > 0){
         List<SequoiaDBOutputField> selectedFields = new ArrayList<SequoiaDBOutputField>();
         for (int i = 0; i < numFields; i++) {
            TableItem item = m_fieldsView.getNonEmpty(i);
            SequoiaDBOutputField fieldTmp = new SequoiaDBOutputField();
            fieldTmp.m_fieldName = item.getText(1).trim();
            fieldTmp.m_path = item.getText(2).trim();
            selectedFields.add(fieldTmp);
         }
         m_meta.setSelectedFields(selectedFields);
      }
   }
   
   private void setSelectedFields(List<SequoiaDBOutputField> fields) {
      if (null == fields) {
         return ;
      }
      
      m_fieldsView.clearAll();
      for (SequoiaDBOutputField f : fields){
         TableItem item = new TableItem(m_fieldsView.table, SWT.NONE);
         
         if(!Const.isEmpty(f.m_fieldName)){
            item.setText(1, f.m_fieldName);
         }
         
         if(!Const.isEmpty(f.m_path)){
            item.setText(2, f.m_path);
         }
      }
      
      m_fieldsView.removeEmptyRows();
      m_fieldsView.setRowNums();
      m_fieldsView.optWidth(true);
   }
}
