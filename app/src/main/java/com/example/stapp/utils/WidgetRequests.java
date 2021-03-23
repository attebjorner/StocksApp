package com.example.stapp.utils;

public class WidgetRequests
{
    private static final String[] WIDGETS = {
//            Stock Chart
            "<!-- TradingView Widget BEGIN -->\n" +
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
            "  \"width\": \"100%%\",\n" +
            "  \"height\": \"100%%\",\n" +
            "  \"locale\": \"en\",\n" +
            "  \"colorTheme\": \"light\",\n" +
            "  \"gridLineColor\": \"%%23F0F3FA\",\n" +
            "  \"trendLineColor\": \"rgba(0, 0, 0, 1)\",\n" +
            "  \"fontColor\": \"%%23787B86\",\n" +
            "  \"underLineColor\": \"rgba(238, 238, 238, 1)\",\n" +
            "  \"isTransparent\": false,\n" +
            "  \"autosize\": true,\n" +
            "  \"container_id\": \"tradingview_f724b\"\n" +
            "}\n" +
            "  );\n" +
            "  </script>\n" +
            "</div>\n" +
            "<!-- TradingView Widget END -->",
//            Recommendations
            "https://widget.finnhub.io/widgets/recommendation?symbol=%s",
//            Profile
            "<!-- TradingView Widget BEGIN -->\n" +
            "<div class=\"tradingview-widget-container\">\n" +
            "  <div class=\"tradingview-widget-container__widget\"></div>\n" +
            "  <div class=\"tradingview-widget-copyright\"><a href=\"https://www.tradingview.com/symbols/%s/\" rel=\"noopener\" target=\"_blank\"><span class=\"blue-text\">%s Profile</span></a> by TradingView</div>\n" +
            "  <script type=\"text/javascript\" src=\"https://s3.tradingview.com/external-embedding/embed-widget-symbol-profile.js\" async>\n" +
            "  {\n" +
            "  \"symbol\": \"%s\",\n" +
            "  \"width\": \"100%%\",\n" +
            "  \"height\": \"100%%\",\n" +
            "  \"colorTheme\": \"light\",\n" +
            "  \"isTransparent\": false,\n" +
            "  \"locale\": \"en\"\n" +
            "}\n" +
            "  </script>\n" +
            "</div>\n" +
            "<!-- TradingView Widget END -->",
//            ESP Estimate
            "https://widget.finnhub.io/widgets/eps-estimate?symbol=%s",
//            Financials
            "<!-- TradingView Widget BEGIN -->\n" +
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
            "  \"width\": \"100%%\",\n" +
            "  \"height\": \"100%%\",\n" +
            "  \"locale\": \"en\"\n" +
            "}\n" +
            "  </script>\n" +
            "</div>\n" +
            "<!-- TradingView Widget END -->"
    };

    public static String getWidget(String symbol, int position)
    {
        return position % 2 == 0
                ? String.format(WIDGETS[position], symbol, symbol, symbol)
                : String.format(WIDGETS[position], symbol);
    }
}
