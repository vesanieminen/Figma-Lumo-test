package com.example.employees;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Route(value = "employees")
@PageTitle("Employees")
public class EmployeeListView extends VerticalLayout {

    public record Employee(String name, String department, String jobTitle, String status, LocalDate startDate) {}

    public EmployeeListView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("view-content");

        add(buildTabs(), buildToolbar(), buildMasterDetail());
    }

    private Component buildTabs() {
        var tabs = new Tabs(new Tab("Employee List"), new Tab("Organization Chart"));
        tabs.setSelectedIndex(0);
        var wrap = new Div(tabs);
        wrap.addClassName("tabs-bar");
        return wrap;
    }

    private Component buildToolbar() {
        var kpis = new Div();
        kpis.addClassName("kpis");

        kpis.add(employeeKpi("Total employees", "246", "+14", " this year", true));
        kpis.add(kpiDivider());
        kpis.add(employeeKpi("Logistics employees", "192", "78%", " of all", false));

        var export = new Button("Export", new Icon(VaadinIcon.DOWNLOAD));
        export.addThemeVariants(ButtonVariant.TERTIARY);
        export.getStyle()
                .set("color", "var(--lumo-primary-text-color)")
                .set("border", "1px solid var(--lumo-contrast-20pct)");

        var add = new Button("Add employee", new Icon(VaadinIcon.PLUS));
        add.addThemeVariants(ButtonVariant.PRIMARY);

        var actions = new HorizontalLayout(export, add);
        actions.setSpacing(true);
        actions.setPadding(false);

        var toolbar = new HorizontalLayout(kpis, actions);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setSpacing(false);
        toolbar.setPadding(false);
        return toolbar;
    }

    private Component employeeKpi(String label, String value, String highlight, String tail, boolean positive) {
        var k = new Div();
        k.addClassName("kpi");

        var labelEl = new Span(label);
        labelEl.addClassName("kpi-label");

        var valueEl = new Span(value);
        valueEl.addClassName("kpi-value");

        var highlightEl = new Span(highlight);
        highlightEl.getStyle()
                .set("color", positive ? "var(--lumo-success-text-color)" : "var(--lumo-primary-text-color)")
                .set("font-weight", "600")
                .set("font-size", "var(--lumo-font-size-s)");
        var tailEl = new Span(tail);
        tailEl.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "var(--lumo-font-size-s)");
        var sub = new Span(highlightEl, tailEl);
        k.add(labelEl, valueEl, sub);
        return k;
    }

    private Component kpiDivider() {
        var d = new Div();
        d.addClassName("kpi-divider");
        return d;
    }

    private Component buildMasterDetail() {
        var master = buildGrid();
        var detail = buildEmployeeForm();

        var split = new Div(master, detail);
        split.addClassName("master-detail");
        split.getStyle().set("flex", "1").set("overflow", "hidden");
        return split;
    }

    private Component buildGrid() {
        Grid<Employee> grid = new Grid<>();
        grid.setAllRowsVisible(false);
        grid.getStyle().set("border-radius", "0").set("border", "none");

        var fmt = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

        grid.addColumn(Employee::name).setHeader("Name").setWidth("160px").setFlexGrow(0);
        grid.addColumn(Employee::department).setHeader("Department").setWidth("110px").setFlexGrow(0);
        grid.addColumn(Employee::jobTitle).setHeader("Job title").setWidth("160px").setFlexGrow(0);
        grid.addComponentColumn(e -> {
            var badge = new Span(e.status());
            badge.addClassName("status-badge");
            switch (e.status()) {
                case "Active" -> {}
                case "Inactive" -> badge.addClassName("inactive");
                default -> badge.addClassName("unrated");
            }
            return badge;
        }).setHeader("Status").setWidth("90px").setFlexGrow(0);
        grid.addColumn(e -> fmt.format(e.startDate())).setHeader("Start date").setWidth("110px").setFlexGrow(0);

        grid.setPartNameGenerator(e -> "Liam Johnson".equals(e.name()) && e.department().equals("Deliveries") && e.startDate().getYear() == 2022 ? "selected-row" : null);

        var employees = sampleEmployees();
        grid.setItems(employees);

        var selected = employees.stream().filter(e -> "Liam Johnson".equals(e.name())).findFirst();
        selected.ifPresent(grid::select);

        var wrap = new Div(grid);
        wrap.addClassName("master-side");
        wrap.getStyle().set("display", "flex").set("flex", "1").set("min-width", "0");
        return wrap;
    }

    private List<Employee> sampleEmployees() {
        return List.of(
                new Employee("Henry Thompson", "Marketing", "Content strategist", "Active", LocalDate.of(2021, 3, 12)),
                new Employee("Liam Johnson", "Deliveries", "Logistics handler", "Active", LocalDate.of(2022, 4, 5)),
                new Employee("Justin Smith", "Deliveries", "Logistics handler", "Active", LocalDate.of(2020, 6, 30)),
                new Employee("Jordan Brown", "Finance", "Accountant", "Active", LocalDate.of(2023, 8, 15)),
                new Employee("Jacob Williams", "HR", "Recruiter", "Active", LocalDate.of(2024, 1, 1)),
                new Employee("Robert Davis", "Deliveries", "Logistics handler", "Inactive", LocalDate.of(2019, 11, 18)),
                new Employee("Maya Garcia", "Deliveries", "Logistics handler", "On leave", LocalDate.of(2022, 9, 9)),
                new Employee("Andrew Martinez", "Deliveries", "Logistics handler", "Active", LocalDate.of(2021, 5, 4)),
                new Employee("Samantha Rodriguez", "Marketing", "Marketing lead", "Active", LocalDate.of(2020, 2, 14)),
                new Employee("Angel Wilson", "Deliveries", "Logistics handler", "Active", LocalDate.of(2024, 3, 22)),
                new Employee("Henry Thompson", "Deliveries", "Logistics handler", "Active", LocalDate.of(2023, 6, 1)),
                new Employee("Liam Johnson", "Finance", "CFO", "Active", LocalDate.of(2018, 10, 10)),
                new Employee("Justin Smith", "Deliveries", "Logistics handler", "Active", LocalDate.of(2022, 12, 5)),
                new Employee("Jordan Brown", "Deliveries", "Logistics handler", "Active", LocalDate.of(2021, 7, 19)),
                new Employee("Jacob Williams", "Deliveries", "Logistics handler", "Inactive", LocalDate.of(2019, 4, 27)),
                new Employee("Robert Davis", "Marketing", "Content strategist", "Active", LocalDate.of(2024, 2, 9)),
                new Employee("Maya Garcia", "Deliveries", "Logistics handler", "Active", LocalDate.of(2023, 11, 11)),
                new Employee("Andrew Martinez", "Deliveries", "Logistics handler", "Active", LocalDate.of(2022, 1, 23)),
                new Employee("Samantha Rodriguez", "Finance", "Finance mager", "Active", LocalDate.of(2020, 8, 14)),
                new Employee("Angel Wilson", "Deliveries", "Logistics handler", "Active", LocalDate.of(2024, 5, 6)));
    }

    private Component buildEmployeeForm() {
        var wrap = new Div();
        wrap.addClassName("detail-side");

        var header = new Div();
        header.addClassName("employee-form-header");

        var titleBlock = new Div();
        var nameH = new H1("Liam Johnson");
        nameH.getStyle().set("margin", "0");
        var service = new Div();
        service.setText("4 years 3 months in service");
        service.addClassName("service-time");
        titleBlock.add(nameH, service);

        var badge = new Span("12 assigned tasks");
        badge.getStyle()
                .set("background", "var(--lumo-primary-color-10pct)")
                .set("color", "var(--lumo-primary-text-color)")
                .set("padding", "4px 12px")
                .set("border-radius", "999px")
                .set("font-size", "var(--lumo-font-size-s)")
                .set("font-weight", "500")
                .set("white-space", "nowrap");

        header.add(titleBlock, badge);

        // Personal info section
        var personal = new Div();
        personal.addClassName("employee-form-section");

        var firstName = new TextField("First name");
        firstName.setValue("Liam");
        var lastName = new TextField("Last name");
        lastName.setValue("Johnson");

        var row1 = new HorizontalLayout(firstName, lastName);
        row1.setWidthFull();
        row1.setSpacing(true);
        row1.setPadding(false);
        firstName.setWidthFull();
        lastName.setWidthFull();

        var phone = new TextField("Phone");
        phone.setValue("+91 555 1213 456");
        phone.setWidthFull();

        var email = new TextField("Email");
        email.setValue("liam.johnson@example.com");
        email.setWidthFull();

        var dob = new DatePicker("Date of Birth");
        var dobI18n = new DatePicker.DatePickerI18n();
        dobI18n.setDateFormat("dd/MM/yyyy");
        dob.setI18n(dobI18n);
        dob.setValue(LocalDate.of(1972, 6, 22));
        dob.setWidth("264px");

        personal.add(row1, phone, email, dob);

        // Role section
        var role = new Div();
        role.addClassName("employee-form-section");

        var roleHeading = new H3("Role");

        var department = new ComboBox<String>("Department");
        department.setItems("Deliveries", "Marketing", "Finance", "HR");
        department.setValue("Deliveries");
        var jobTitle = new ComboBox<String>("Job title");
        jobTitle.setItems("Logistics handler", "Accountant", "CFO", "Content strategist", "Recruiter",
                "Marketing lead", "Finance manager");
        jobTitle.setValue("Logistics handler");

        var roleRow = new HorizontalLayout(department, jobTitle);
        roleRow.setWidthFull();
        roleRow.setSpacing(true);
        roleRow.setPadding(false);
        department.setWidthFull();
        jobTitle.setWidthFull();

        var status = new RadioButtonGroup<String>();
        status.setLabel("Status");
        status.setItems("Active", "On leave", "Inactive");
        status.setValue("Active");

        role.add(roleHeading, roleRow, status);

        // Footer
        var remove = new Button("Remove");
        remove.addThemeVariants(ButtonVariant.TERTIARY);
        remove.getStyle()
                .set("color", "var(--lumo-error-text-color)")
                .set("border", "1px solid var(--lumo-contrast-20pct)");

        var cancel = new Button("Cancel");
        cancel.addThemeVariants(ButtonVariant.TERTIARY);
        cancel.getStyle()
                .set("color", "var(--lumo-primary-text-color)")
                .set("border", "1px solid var(--lumo-contrast-20pct)");

        var save = new Button("Save changes");
        save.addThemeVariants(ButtonVariant.PRIMARY);

        var rightActions = new HorizontalLayout(cancel, save);
        rightActions.setSpacing(true);
        rightActions.setPadding(false);
        rightActions.addClassName("right-actions");

        var footer = new HorizontalLayout(remove, rightActions);
        footer.addClassName("form-footer");
        footer.setWidthFull();
        footer.setSpacing(false);
        footer.setPadding(false);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        wrap.add(header, personal, role, footer);
        return wrap;
    }
}
