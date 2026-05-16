package com.example.base.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createLogoBlock(), createNavScroller(), createUserButton());
    }

    private Component createLogoBlock() {
        var mark = new Div();
        mark.addClassName("acme-logo-mark");

        var name = new Span("ACME");
        var corp = new Span("CORP");
        corp.getStyle().set("color", "rgba(255,255,255,0.7)").set("margin-inline-start", "4px");

        var logoRow = new HorizontalLayout(mark, name, corp);
        logoRow.setAlignItems(FlexComponent.Alignment.CENTER);
        logoRow.setSpacing(false);
        logoRow.setPadding(false);
        logoRow.getStyle().set("gap", "8px");
        logoRow.addClassName("acme-logo");

        var block = new Div(logoRow);
        block.addClassName("acme-logo-block");
        return block;
    }

    private Component createNavScroller() {
        var navWrap = new VerticalLayout();
        navWrap.setPadding(false);
        navWrap.setSpacing(false);
        navWrap.getStyle().set("padding", "0 12px");

        // Top: Dashboard
        var topNav = new SideNav();
        topNav.addItem(new SideNavItem("Dashboard", "dashboard", new Icon(VaadinIcon.DASHBOARD)));
        navWrap.add(topNav);

        // Sales group
        navWrap.add(groupHeading("Sales"));
        var salesNav = new SideNav();
        salesNav.addItem(new SideNavItem("Orders", "orders", new Icon(VaadinIcon.CART)));
        salesNav.addItem(new SideNavItem("Deliveries", "deliveries", new Icon(VaadinIcon.TRUCK)));
        salesNav.addItem(new SideNavItem("Reports", "", new Icon(VaadinIcon.CHART_LINE)));
        navWrap.add(salesNav);

        // Resources group
        navWrap.add(groupHeading("Resources"));
        var resNav = new SideNav();
        resNav.addItem(new SideNavItem("Employees", "employees", new Icon(VaadinIcon.USERS)));
        resNav.addItem(new SideNavItem("Utilisation", "utilisation", new Icon(VaadinIcon.PIE_CHART)));
        resNav.addItem(new SideNavItem("Payroll", "payroll", new Icon(VaadinIcon.MONEY)));
        navWrap.add(resNav);

        // Admin group
        navWrap.add(groupHeading("Admin"));
        var adminNav = new SideNav();
        adminNav.addItem(new SideNavItem("Access management", "access", new Icon(VaadinIcon.KEY)));
        adminNav.addItem(new SideNavItem("Settings", "settings", new Icon(VaadinIcon.COG)));
        navWrap.add(adminNav);

        var scroller = new Scroller(navWrap);
        scroller.setHeightFull();
        return scroller;
    }

    private Component groupHeading(String label) {
        var h = new H4(label);
        h.addClassName("nav-group-title");
        return h;
    }

    private Component createUserButton() {
        var avatar = new Avatar("Firstname Lastname");
        avatar.getStyle().set("--vaadin-avatar-size", "32px");

        var name = new Span("Firstname Lastname");
        name.addClassName("user-name");

        var chevron = new Icon(VaadinIcon.CHEVRON_UP_SMALL);

        var button = new Div(avatar, name, chevron);
        button.addClassName("user-button");
        return button;
    }
}
