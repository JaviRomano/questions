export function loadHeader() {
    const head = document.head;
	
    const metaCharset = document.createElement("meta");
    metaCharset.setAttribute("charset", "UTF-8");

    const metaViewport = document.createElement("meta");
    metaViewport.setAttribute("name", "viewport");
    metaViewport.setAttribute("content", "width=device-width, initial-scale=1.0");

    const customStyles = document.createElement("link");
    customStyles.setAttribute("rel", "stylesheet");
    customStyles.setAttribute("href", "/css/styles.css");

    const bootstrapCSS = document.createElement("link");
    bootstrapCSS.setAttribute("rel", "stylesheet");
    bootstrapCSS.setAttribute("href", "https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css");

    const fontAwesome = document.createElement("link");
    fontAwesome.setAttribute("rel", "stylesheet");
    fontAwesome.setAttribute("href", "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css");

    const sweetAlertCSS = document.createElement("link");
    sweetAlertCSS.setAttribute("rel", "stylesheet");
    sweetAlertCSS.setAttribute("href", "https://cdn.jsdelivr.net/npm/sweetalert2@11.7.0/dist/sweetalert2.min.css");

    head.appendChild(metaCharset);
    head.appendChild(metaViewport);
    head.appendChild(customStyles);
    head.appendChild(bootstrapCSS);
    head.appendChild(fontAwesome);
    head.appendChild(sweetAlertCSS);
}