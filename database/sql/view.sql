CREATE VIEW Mois AS
WITH RECURSIVE MoisRec AS (
    SELECT 1 AS numero, 'Janvier' AS nom
    UNION ALL
    SELECT numero + 1, 
        CASE numero + 1
            WHEN 2 THEN 'Février'
            WHEN 3 THEN 'Mars'
            WHEN 4 THEN 'Avril'
            WHEN 5 THEN 'Mai'
            WHEN 6 THEN 'Juin'
            WHEN 7 THEN 'Juillet'
            WHEN 8 THEN 'Août'
            WHEN 9 THEN 'Septembre'
            WHEN 10 THEN 'Octobre'
            WHEN 11 THEN 'Novembre'
            WHEN 12 THEN 'Décembre'
        END
    FROM MoisRec
    WHERE numero < 12
)
SELECT numero AS mois_num, nom AS mois_nom
FROM MoisRec;


--------------------------------------------Annee et Mois----------------------------------------------------------------------
CREATE OR REPLACE VIEW V_annees_et_mois AS
WITH Years AS (
    SELECT DISTINCT EXTRACT(YEAR FROM date_insertion) AS annee FROM Utilisateur
    UNION
    SELECT DISTINCT EXTRACT(YEAR FROM date_achat) AS annee FROM achat_pack
),
Months AS (
    SELECT Mois.mois_num, Mois.mois_nom FROM Mois
)
SELECT
    y.annee,
    m.mois_num,
    m.mois_nom
FROM
    Years y
CROSS JOIN
    Months m;

---------------------------------------------Evolutions des nombres de reservations mensuels-------------------------------------
CREATE OR REPLACE VIEW V_evolutions_reservations_mensuelles AS
WITH Reservations AS (
    SELECT 
        EXTRACT(YEAR FROM date_validation) AS annee,
        EXTRACT(MONTH FROM date_validation) AS mois,
        COUNT(*) AS nombre_reservations
    FROM Reservation
    WHERE date_validation IS NOT NULL
    GROUP BY EXTRACT(YEAR FROM date_validation), EXTRACT(MONTH FROM date_validation)
)
SELECT
    am.annee,
    am.mois_num,
    am.mois_nom as mois,
    COALESCE(r.nombre_reservations, 0) AS nombre_reservations
FROM
    V_annees_et_mois am
LEFT JOIN
    Reservations r ON am.annee = r.annee AND am.mois_num = r.mois
ORDER BY annee;

---------------------------------------------Evolutions des nombres de reservations hotel mensuels-------------------------------------


CREATE OR REPLACE VIEW V_evolutions_reservations_hotel_mensuelles AS
WITH Reservations AS (
    SELECT 
        h.nom_hotel,
        EXTRACT(YEAR FROM r.date_validation) AS annee,
        EXTRACT(MONTH FROM r.date_validation) AS mois,
        COUNT(*) AS nombre_reservations
    FROM 
        Reservation r
    JOIN 
        Chambre c ON r.id_chambre = c.id_chambre
    JOIN 
        Hotel h ON c.id_hotel = h.id_hotel
    WHERE 
        r.date_validation IS NOT NULL

    GROUP BY 
        h.nom_hotel, EXTRACT(YEAR FROM r.date_validation), EXTRACT(MONTH FROM r.date_validation)
)
SELECT
    am.annee,
    am.mois_num,
    am.mois_nom as mois,
    COALESCE(r.nom_hotel, 'Aucun') AS nom_hotel,
    COALESCE(r.nombre_reservations, 0) AS nombre_reservations
FROM
    V_annees_et_mois am
LEFT JOIN
    Reservations r ON am.annee = r.annee AND am.mois_num = r.mois
ORDER BY annee;

-----------------------------------------Evolutions des nombres de reservations transporrt mensuels------------------------------------------
CREATE OR REPLACE VIEW V_evolution_reservations_transport AS
WITH Reservations AS (
    SELECT
        t.nom_transport AS nom_transport,
        EXTRACT(YEAR FROM r.date_validation) AS annee,
        EXTRACT(MONTH FROM r.date_validation) AS mois,
        COUNT(r.id_reservation) AS nombre_reservations
    FROM
        Reservation r
    JOIN
        Categorie_reservation cr ON r.id_categorie_reservation = cr.id_categorie_reservation
    JOIN
        Transport t ON r.id_partenaire = t.id_transport
    WHERE 
        cr.libelle = 'Transport' AND
        r.date_validation IS NOT NULL
    GROUP BY
        t.nom_transport, EXTRACT(YEAR FROM r.date_validation), EXTRACT(MONTH FROM r.date_validation)
)
SELECT
    v.annee,
    v.mois_num,
    v.mois_nom AS mois,
    COALESCE(r.nom_transport, 'Aucun') AS nom_transport,
    COALESCE(r.nombre_reservations, 0) AS nombre_reservations
FROM
    V_annees_et_mois v
LEFT JOIN
    Reservations r ON v.annee = r.annee AND v.mois_num = r.mois
ORDER BY
    v.annee, v.mois_num, r.nom_transport;



--------------------------------------------------------V_HOTEL------------------------------------------------------------------------
CREATE OR REPLACE VIEW V_HOTEL AS
SELECT 
    'hotel' AS type,
    h.nom_hotel AS nom,
    h.description,
    h.adresse_hotel AS adresse,
    v.nom_ville AS ville,
    r.nom_region AS region,
    h.id_hotel AS id,
    c.tarif AS prix,
    h.date_insertion
FROM 
    Hotel h
JOIN 
    Chambre c ON h.id_hotel = c.id_hotel
JOIN 
    Ville v ON h.id_ville = v.id_ville
JOIN 
    region r ON v.id_region = r.id_region;


--------------------------------------------------------V_ATTRACTION-------------------------------------------------------------------

CREATE OR REPLACE VIEW V_ATTRACTION AS
SELECT 
    'attraction' AS type,
    a.nom_attraction AS nom,
    a.description,
    v.nom_ville AS ville,
    r.nom_region AS region,
    a.id_attraction AS id,
    a.prix AS prix,
    a.date_insertion
FROM 
    Attraction a
JOIN 
    Ville v ON a.id_ville = v.id_ville
JOIN 
    region r ON v.id_region = r.id_region;

----------------------------------------------------------V_TRANSPORT--------------------------------------------------------------------
CREATE OR REPLACE VIEW V_TRANSPORT AS
SELECT 
    'transport' AS type,
    t.nom_transport AS nom,
    t.description,
    '' AS adresse,
    v.nom_ville AS ville,
    t.id_transport AS id,
    t.tarif AS prix,
    t.date_insertion
FROM
    Transport t
LEFT JOIN
    Ville v ON t.id_ville = v.id_ville;


-----------------------------------------------------------V_PACK-----------------------------------------------------------------------

CREATE OR REPLACE VIEW V_PACK AS
SELECT
    'pack' AS type,
    p.nom_pack AS nom,
    '' AS description,
    '' AS adresse,
    
    r.nom_region AS region,
    v.nom_ville AS ville,
    p.id_pack AS id,
    MIN(
        COALESCE(pt.tarif, 0) +
        COALESCE(a.prix, 0) +
        COALESCE(c.tarif, 0)
    ) AS prix,
    p.date_insertion
FROM
    pack p
LEFT JOIN
    Transport pt ON p.id_transport = pt.id_transport
LEFT JOIN
    Attraction a ON p.id_attraction = a.id_attraction
LEFT JOIN
    Chambre c ON p.id_hotel = c.id_hotel
LEFT JOIN
    Hotel h ON c.id_hotel = h.id_hotel
LEFT JOIN
    Ville v ON h.id_ville = v.id_ville
LEFT JOIN
    region r ON v.id_region = r.id_region

GROUP BY
    p.id_pack, p.nom_pack, p.date_insertion, r.nom_region,v.nom_ville;



-----------------------------------------------------------V_GUIDE----------------------------------------------------------------------

CREATE OR REPLACE VIEW V_GUIDE AS
SELECT
    'guide' AS type,
    g.nom_guide AS nom,
    g.description,
    '' AS adresse,
    v.nom_ville AS ville,
    g.id_guide AS id,
    0 AS prix,
    g.date_insertion
FROM
    Guide g
JOIN
    partenaire p ON g.id_partenaire = p.id_partenaire
LEFT JOIN
    Ville v ON g.id_ville = v.id_ville






CREATE OR REPLACE VIEW suggestions_par_budget AS
SELECT
    pack.id AS id_pack,
    villes.ville AS ville,
    COALESCE(hotel.nombre_hotels, 0) AS nombre_hotels,
    COALESCE(attraction.nombre_attractions, 0) AS nombre_attractions,
    COALESCE(transport.nombre_transports, 0) AS nombre_transports,
    COALESCE(guide.nombre_guides, 0) AS nombre_guides,
    pack.nom AS nom_pack,
    COALESCE(hotel.prix, 0) + COALESCE(attraction.prix, 0) + COALESCE(transport.prix, 0) + COALESCE(pack.prix, 0) AS prix_total
FROM
    (SELECT DISTINCT ville FROM V_HOTEL) AS villes
LEFT JOIN
    (SELECT ville, COUNT(*) AS nombre_hotels, SUM(prix) AS prix FROM V_HOTEL GROUP BY ville) AS hotel
    ON villes.ville = hotel.ville
LEFT JOIN
    (SELECT ville, COUNT(*) AS nombre_attractions, SUM(prix) AS prix FROM V_ATTRACTION GROUP BY ville) AS attraction
    ON villes.ville = attraction.ville
LEFT JOIN
    (SELECT ville, COUNT(*) AS nombre_transports, SUM(prix) AS prix FROM V_TRANSPORT GROUP BY ville) AS transport
    ON villes.ville = transport.ville
LEFT JOIN
    (SELECT id, ville, nom, prix FROM V_PACK) AS pack
    ON villes.ville = pack.ville
LEFT JOIN
    (SELECT ville, COUNT(*) AS nombre_guides, SUM(prix) AS prix FROM V_GUIDE GROUP BY ville) AS guide
    ON villes.ville = guide.ville
ORDER BY villes.ville;


SELECT *
FROM suggestions_par_budget

WHERE id_pack IS NOT NULL AND prix_total <= 3000000;
