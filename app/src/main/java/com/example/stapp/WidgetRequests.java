package com.example.stapp;

public class WidgetRequests
{
    private static final String STOCK_CHART = "<!-- TradingView Widget BEGIN -->\n" +
            "<div class=\"tradingview-widget-container\">\n" +
            "  <div id=\"tradingview_f724b\"></div>\n" +
            "  <div class=\"tradingview-widget-copyright\"><a href=\"https://www.tradingview.com/symbols/%s/\" rel=\"noopener\" target=\"_blank\"><span class=\"blue-text\">%s Stock Price Today</span></a> by TradingView</div>\n" +
            "  <script type=\"text/javascript\" src=\"https://s3.tradingview.com/tv.js\"></script>\n" +
            "  <script type=\"text/javascript\">\n" +
            "  new TradingView.MediumWidget(\n" +
            "  {\n" +
            "  \"symbols\": [\n" +
            "    [\n" +
            "      \"%s|12M\"\n" +
            "    ]\n" +
            "  ],\n" +
            "  \"chartOnly\": false,\n" +
            "  \"width\": \"100%\",\n" +
            "  \"height\": \"100%\",\n" +
            "  \"locale\": \"en\",\n" +
            "  \"colorTheme\": \"light\",\n" +
            "  \"gridLineColor\": \"#F0F3FA\",\n" +
            "  \"trendLineColor\": \"rgba(0, 0, 0, 1)\",\n" +
            "  \"fontColor\": \"#787B86\",\n" +
            "  \"underLineColor\": \"rgba(238, 238, 238, 1)\",\n" +
            "  \"isTransparent\": false,\n" +
            "  \"autosize\": true,\n" +
            "  \"container_id\": \"tradingview_f724b\"\n" +
            "}\n" +
            "  );\n" +
            "  </script>\n" +
            "</div>\n" +
            "<!-- TradingView Widget END -->";

    private static final String PROFILE = "<!-- TradingView Widget BEGIN -->\n" +
            "<div class=\"tradingview-widget-container\">\n" +
            "  <div class=\"tradingview-widget-container__widget\"></div>\n" +
            "  <div class=\"tradingview-widget-copyright\"><a href=\"https://www.tradingview.com/symbols/%s/\" rel=\"noopener\" target=\"_blank\"><span class=\"blue-text\">%s Profile</span></a> by TradingView</div>\n" +
            "  <script type=\"text/javascript\" src=\"https://s3.tradingview.com/external-embedding/embed-widget-symbol-profile.js\" async>\n" +
            "  {\n" +
            "  \"symbol\": \"%s\",\n" +
            "  \"width\": 480,\n" +
            "  \"height\": 650,\n" +
            "  \"colorTheme\": \"light\",\n" +
            "  \"isTransparent\": false,\n" +
            "  \"locale\": \"en\"\n" +
            "}\n" +
            "  </script>\n" +
            "</div>\n" +
            "<!-- TradingView Widget END -->";

    private static final String FINANCIALS = "<!-- TradingView Widget BEGIN -->\n" +
            "<div class=\"tradingview-widget-container\">\n" +
            "  <div class=\"tradingview-widget-container__widget\"></div>\n" +
            "  <div class=\"tradingview-widget-copyright\"><a href=\"https://www.tradingview.com/symbols/%s/\" rel=\"noopener\" target=\"_blank\"><span class=\"blue-text\">%s Fundamental Data</span></a> by TradingView</div>\n" +
            "  <script type=\"text/javascript\" src=\"https://s3.tradingview.com/external-embedding/embed-widget-financials.js\" async>\n" +
            "  {\n" +
            "  \"symbol\": \"%s\",\n" +
            "  \"colorTheme\": \"light\",\n" +
            "  \"isTransparent\": false,\n" +
            "  \"largeChartUrl\": \"\",\n" +
            "  \"displayMode\": \"regular\",\n" +
            "  \"width\": \"100%\",\n" +
            "  \"height\": \"100%\",\n" +
            "  \"locale\": \"en\"\n" +
            "}\n" +
            "  </script>\n" +
            "</div>\n" +
            "<!-- TradingView Widget END -->";

    private static final String RECOMMENDATIONS = "https://widget.finnhub.io/widgets/recommendation?symbol=%s";
    private static final String ESP_ESTIMATES = "https://widget.finnhub.io/widgets/eps-estimate?symbol=%s";

    public static String getStockChart(String stock)
    {
        return String.format(STOCK_CHART, stock, stock, stock);
    }

    public static String getProfile(String stock)
    {
        return String.format(PROFILE, stock, stock, stock);
    }

    public static String getFinancials(String stock)
    {
        return String.format(FINANCIALS, stock, stock, stock);
    }

    public static String getRecommendations(String stock)
    {
        return String.format(RECOMMENDATIONS, stock);
    }

    public static String getEspEstimates(String stock)
    {
        return String.format(ESP_ESTIMATES, stock);
    }
}
