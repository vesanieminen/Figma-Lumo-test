package com.example.reports;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "")
@PageTitle("Reports")
public class ReportsView extends VerticalLayout {

    private record Report(String title, String period, String image, String badge) {}

    public ReportsView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("view-content");

        add(buildHeader());
        add(buildToolbar());
        add(buildBody());
    }

    private Component buildHeader() {
        var title = new H1("Reports");
        title.addClassName("view-title");
        var header = new Div(title);
        header.addClassName("view-header");
        return header;
    }

    private Component buildToolbar() {
        var kpis = new Div();
        kpis.addClassName("kpis");
        kpis.add(kpi("2026 average sales", "168 640 €"));
        kpis.add(kpiDivider());
        kpis.add(kpi("March 2026 sales", "174 610 €"));
        kpis.add(kpiDivider());
        kpis.add(kpi("February 2026 sales", "127 080 €"));

        var newReport = new Button("New report", new Icon(VaadinIcon.PLUS));
        newReport.addThemeVariants(ButtonVariant.TERTIARY);
        newReport.getStyle()
                .set("color", "var(--lumo-primary-text-color)")
                .set("font-weight", "600");

        var toolbar = new HorizontalLayout(kpis, newReport);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.setPadding(false);
        toolbar.setSpacing(false);
        return toolbar;
    }

    private Component kpi(String label, String value) {
        var k = new Div();
        k.addClassName("kpi");
        var l = new Span(label);
        l.addClassName("kpi-label");
        var v = new Span(value);
        v.addClassName("kpi-value");
        k.add(l, v);
        return k;
    }

    private Component kpiDivider() {
        var d = new Div();
        d.addClassName("kpi-divider");
        return d;
    }

    private Component buildBody() {
        var filters = buildFilters();
        var cards = buildCardGrid();

        var body = new HorizontalLayout(filters, cards);
        body.addClassName("view-body");
        body.setWidthFull();
        body.setSpacing(false);
        body.setPadding(false);
        body.setFlexGrow(1, cards);
        body.getStyle().set("flex", "1").set("overflow", "hidden");
        return body;
    }

    private Component buildFilters() {
        var panel = new Div();
        panel.addClassName("filters-panel");

        var title = new H3("Filters");
        var search = new TextField("Free search");
        search.setPlaceholder("");
        search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        search.setWidthFull();

        var regions = new MultiSelectComboBox<String>("Regions");
        regions.setItems("Nordics", "Central Europe", "Western Europe", "Eastern Europe", "Asia");
        regions.setValue(java.util.Set.of("Nordics", "Central Europe"));
        regions.setWidthFull();

        var dateRange = new DateRangeField();
        dateRange.setLabel("Date range");
        dateRange.setWidthFull();

        panel.add(title, search, regions, dateRange);
        return panel;
    }

    private Component buildCardGrid() {
        var scroll = new Div();
        scroll.addClassName("cards-scroll");

        var grid = new Div();
        grid.addClassName("card-grid");

        List<Report> reports = List.of(
                new Report("Deutschland", "March 2026", "images/reports/deutschland.jpg", "Unread"),
                new Report("Czechia", "March 2026", "images/reports/czechia.jpg", "Unread"),
                new Report("Sweden", "March 2026", "images/reports/sweden.jpg", null),
                new Report("Austria", "March 2026", "images/reports/austria.jpg", null),
                new Report("Finland", "March 2026", "images/reports/finland.jpg", null),
                new Report("Deutschland", "February 2026", "images/reports/deutschland.jpg", null),
                new Report("Czechia", "February 2026", "images/reports/czechia.jpg", null),
                new Report("Norway", "February 2026", "images/reports/norway.jpg", null),
                new Report("Sweden", "February 2026", "images/reports/sweden.jpg", null),
                new Report("Austria", "February 2026", "images/reports/austria.jpg", null),
                new Report("Finland", "February 2026", "images/reports/finland.jpg", null));

        reports.forEach(r -> grid.add(buildCard(r)));
        scroll.add(grid);
        return scroll;
    }

    private Component buildCard(Report report) {
        var card = new Div();
        card.addClassName("card");

        var image = new Image(report.image(), report.title());
        image.addClassName("card-media");

        var titleEl = new Span(report.title());
        titleEl.addClassName("card-title");
        titleEl.getElement().getStyle().set("display", "block");
        var sub = new Span(report.period());
        sub.addClassName("card-subtitle");
        sub.getElement().getStyle().set("display", "block");

        var titleWrap = new Div(titleEl, sub);
        titleWrap.getStyle().set("flex", "1");

        var content = new Div(titleWrap);
        content.addClassName("card-content");

        if (report.badge() != null) {
            var badge = new Span(report.badge());
            badge.addClassName("status-badge");
            badge.getStyle()
                    .set("background", "var(--lumo-primary-color-10pct)")
                    .set("color", "var(--lumo-primary-text-color)");
            content.add(badge);
        }

        card.add(image, content);
        return card;
    }

    /** Inline date-range custom field: two date pickers separated by a dash. */
    private static class DateRangeField extends CustomField<DateRange> {
        private final DatePicker from = new DatePicker();
        private final DatePicker to = new DatePicker();

        DateRangeField() {
            from.setPlaceholder("From");
            to.setPlaceholder("To");
            var dash = new Span("-");
            dash.getStyle().set("color", "var(--lumo-secondary-text-color)").set("padding", "0 8px");
            var row = new HorizontalLayout(from, dash, to);
            row.setAlignItems(Alignment.CENTER);
            row.setSpacing(false);
            row.setPadding(false);
            row.getStyle().set("gap", "0");
            add(row);
        }

        @Override
        protected DateRange generateModelValue() {
            return new DateRange(from.getValue(), to.getValue());
        }

        @Override
        protected void setPresentationValue(DateRange newPresentationValue) {
            if (newPresentationValue == null) {
                from.clear();
                to.clear();
            } else {
                from.setValue(newPresentationValue.from());
                to.setValue(newPresentationValue.to());
            }
        }
    }

    private record DateRange(java.time.LocalDate from, java.time.LocalDate to) {}
}
