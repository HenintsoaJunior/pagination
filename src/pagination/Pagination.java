package pagination;

import java.util.ArrayList;
import java.util.List;

public class Pagination {
    private int totalEnregistrements = 0;
    private int totalParPage = 10;
    private int totalPages = 1;
    private int pageActuelle = 1;
    private String classesCSS = "";
    private String url = "#";
    private String parametres = ""; 

    public int getTotalEnregistrements() {
        return totalEnregistrements;
    }

    public void setTotalEnregistrements(int totalEnregistrements) {
        this.totalEnregistrements = totalEnregistrements;
    }

    public int getTotalParPage() {
        return totalParPage;
    }

    public void setTotalParPage(int totalParPage) {
        this.totalParPage = totalParPage;
    }

    public int getPageActuelle() {
        return pageActuelle;
    }

    public void setPageActuelle(String pageString) {
        try {
            this.pageActuelle = Integer.parseInt(pageString);
        } catch (NumberFormatException ex) {
            this.pageActuelle = 1;
        }
    }
    

    public String getClassesCSS() {
        return classesCSS;
    }

    public void setClassesCSS(String classesCSS) {
        this.classesCSS = classesCSS;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParametres() {
        return parametres;
    }

    public void setParametres(String parametres) {
        this.parametres = parametres;
    }
    
    public String paginationNumerique() {
        String retour = "";
        this.calculerTotalPages();
        
        if(totalPages > 1) {
            retour = "<ul class=\"pagination " + this.getClassesCSS() + " \">\n";
            retour += this.boutonPremiere();
            retour += this.boutonPrecedente();
            retour += this.boutonNumeros();
            retour += this.boutonSuivante();
            retour += this.boutonDerniere();
            retour += "</ul>";
        }
        return retour;
    }
    
    public String paginationSuivantePrecedente() {
        String retour = "";
        this.calculerTotalPages();
        
        if(totalPages > 1) {
            retour = "<ul class=\"pagination " + this.getClassesCSS() + " \">\n";
            retour += this.boutonPrecedente2();
            retour += this.boutonSuivante2();
            retour += "</ul>";
        }
        return retour;
    }
    
    private String boutonPremiere() {
        String retour = "";
        String desactivee = "";
        String url = this.getUrl() + "?page=1" + getParametres();
         
        if(this.getPageActuelle() <= 1) {
            desactivee = "class=\"disabled\"";
        }
         
        retour = "<li " + desactivee + "><a href=\"" + url + "\">&larr; First</a></li>\n";
        return retour;
    }
     
    private String boutonPrecedente() {
        String retour = "";
        String desactivee = "";
        String url = "";
         
        if(this.getPageActuelle() <= 1) {
            desactivee = "class=\"disabled\"";
            url = this.getUrl() + "?page=1" + getParametres();
        } else {
            int pagePrecedente = this.getPageActuelle() - 1;
            url = this.getUrl() + "?page=" + pagePrecedente;
        }
         
        retour = "<li " + desactivee + "><a href=\"" + url + "\">&larr;</a></li>\n";
        return retour;
    }
    
    private String boutonNumeros() {
        String retour = "";
        String url = "";
        
        if(totalPages <= 10) {
            for(int total = 1; total <= totalPages; total++) {
                url = this.getUrl() + "?page=" + total + getParametres();
                if(total == this.getPageActuelle()) {
                    retour += " <li class=\"active\"><a href=\"" + url + "\">" + total + "</a></li>";
                } else {
                    retour += " <li><a href=\"" + url + "\">" + total + "</a></li>";
                }
            }
        } else {
            int surplusAvant = 0;
            int surplusApres = 0;

            for(int total = (this.getPageActuelle() - 4); total <= (this.getPageActuelle() - 1); total++) {
                if(!(total >= 1)) {
                    surplusAvant++;
                }
            }

            for(int total = (this.getPageActuelle() + 1); total <= (this.getPageActuelle() + 4); total++) {
                if(!(total <= totalPages)) {
                    surplusApres++;
                }
            }

            for(int total = (this.getPageActuelle() - 4) - surplusApres; total <= (this.getPageActuelle() - 1); total++) {
                if(total >= 1) {
                    url = this.getUrl() + "?page=" + total + getParametres();
                    retour += " <li><a href=\"" + url + "\">" + total + "</a></li>\n";
                }
            }
            
            url = this.getUrl() + "?page=" + this.getPageActuelle() + getParametres();
            retour += " <li class=\"active\"><a href=\"" + url + "\">" + this.getPageActuelle() + "</a></li>\n";
            
            for(int total = (this.getPageActuelle() + 1); total <= (this.getPageActuelle() + 4) + surplusAvant; total++) {
                if(total <= totalPages) {
                    url = this.getUrl() + "?page=" + total + getParametres();
                    retour += " <li><a href=\"" + url + "\">" + total + "</a></li>\n";
                }
            }
        }
        return retour;
    }
    
    private String boutonSuivante() {
        String retour = "";
        String desactivee = "";
        String url = "";
         
        if(this.getPageActuelle() == totalPages) {
            desactivee = "class=\"disabled\"";
            url = this.getUrl() + "?page=" + totalPages + getParametres();
        } else {
            int pageSuivante = this.getPageActuelle() + 1;
            url = this.getUrl() + "?page=" + pageSuivante + getParametres();
        }
        retour = "<li " + desactivee + "><a href=\"" + url + "\">&rarr;</a></li>\n";
        return retour;
    }     
     
    private String boutonDerniere() {
        String retour = "";
        String desactivee = "";
        String url = this.getUrl() + "?page=" + totalPages + getParametres();
         
        if(this.getPageActuelle() == totalPages) {
            desactivee = "class=\"disabled\"";
        }
        retour = "<li " + desactivee + "><a href=\"" + url + "\">Next &rarr;</a></li>\n";
        return retour;
    }
     
    private String boutonSuivante2() {
        String retour = "";
        String desactivee = "";
        String url = "";
         
        if(this.getPageActuelle() == totalPages) {
            desactivee = "class=\"disabled\"";
            url = this.getUrl() + "?page=" + totalPages;
        } else {
            int pageSuivante = this.getPageActuelle() + 1;
            url = this.getUrl() + "?page=" + pageSuivante + getParametres();
        }
        retour = "<li " + desactivee + "><a href=\"" + url + "\">Next »</a></li>\n";
        return retour;
    }
    
    private String boutonPrecedente2() {
        String retour = "";
        String desactivee = "";
        String url = "";
         
        if(this.getPageActuelle() <= 1) {
            desactivee = "class=\"disabled\"";
            url = this.getUrl() + "?page=1" + getParametres();
        } else {
            int pagePrecedente = this.getPageActuelle() - 1;
            url = this.getUrl() + "?page=" + pagePrecedente + getParametres();
        }
        retour = "<li " + desactivee + "><a href=\"" + url + "\">« Previous</a></li>\n";
        return retour;
    }
    
    private void calculerTotalPages() {
        double resultat = Math.ceil((double)this.getTotalEnregistrements() / (double)this.getTotalParPage());
        this.totalPages = (int) resultat;
    }
    
    /* Si vous utilisez Oracle pour effectuer la requête, cette méthode retourne la première et la dernière ligne de la requête basée sur la pagination */
    public int[] retourLignesRequeteOracle() {
        int[] retour = new int[2];
        retour[0] = (pageActuelle - 1) * totalParPage;
        retour[1] = pageActuelle * totalParPage;
        return retour;
    }

    public static <T> List<T> paginateList(List<T> fullList, int page, int recordsPerPage) {
        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, fullList.size());
        return new ArrayList<>(fullList.subList(start, end));
    }

    public static int calculateTotalPages(int totalRecords, int recordsPerPage) {
        return (int) Math.ceil((double) totalRecords / recordsPerPage);
    }

    public static int getPageNumber(String pageStr) {
        return (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;
    }
    
}
