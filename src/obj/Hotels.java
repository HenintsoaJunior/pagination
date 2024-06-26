package obj;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.Connexion;

public class Hotels{
	String id_hotel;
	String nom_hotel;
	String description;
	String adresse_hotel;
	Date date_insertion;
	String id_partenaire;
	String id_categorie_hotel;
	String id_ville;

	public Hotels() throws Exception {
	}

	public Hotels(String id_hotel, String nom_hotel, String description, String adresse_hotel, Date date_insertion, String id_partenaire, String id_categorie_hotel, String id_ville) throws Exception {
		this.id_hotel = id_hotel;
		this.nom_hotel = nom_hotel;
		this.description = description;
		this.adresse_hotel = adresse_hotel;
		this.date_insertion = date_insertion;
		this.id_partenaire = id_partenaire;
		this.id_categorie_hotel = id_categorie_hotel;
		this.id_ville = id_ville;
	}


	public void setId_hotel(String newId_hotel) throws Exception {
		this.id_hotel = newId_hotel;
	}

	public void setNom_hotel(String newNom_hotel) throws Exception {
		this.nom_hotel = newNom_hotel;
	}

	public void setDescription(String newDescription) throws Exception {
		this.description = newDescription;
	}

	public void setAdresse_hotel(String newAdresse_hotel) throws Exception {
		this.adresse_hotel = newAdresse_hotel;
	}

	public void setDate_insertion(Date newDate_insertion) throws Exception {
		this.date_insertion = newDate_insertion;
	}

	public void setId_partenaire(String newId_partenaire) throws Exception {
		this.id_partenaire = newId_partenaire;
	}

	public void setId_categorie_hotel(String newId_categorie_hotel) throws Exception {
		this.id_categorie_hotel = newId_categorie_hotel;
	}

	public void setId_ville(String newId_ville) throws Exception {
		this.id_ville = newId_ville;
	}

	public String getId_hotel(){
		return this.id_hotel;
	}

	public String getNom_hotel(){
		return this.nom_hotel;
	}

	public String getDescription(){
		return this.description;
	}

	public String getAdresse_hotel(){
		return this.adresse_hotel;
	}

	public Date getDate_insertion(){
		return this.date_insertion;
	}

	public String getId_partenaire(){
		return this.id_partenaire;
	}

	public String getId_categorie_hotel(){
		return this.id_categorie_hotel;
	}

	public String getId_ville(){
		return this.id_ville;
	}


	public List<Hotels> getAll() throws Exception {
		List<Hotels> hotels = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
	
		try {
			connection = Connexion.getConnection();
			String query = "SELECT * FROM Hotel";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();
	
			while (resultSet.next()) {
				Hotels obj = new Hotels(
					resultSet.getString("id_hotel"),
					resultSet.getString("nom_hotel"),
					resultSet.getString("description"),
					resultSet.getString("adresse_hotel"),
					resultSet.getDate("date_insertion"),
					resultSet.getString("id_partenaire"),
					resultSet.getString("id_categorie_hotel"),
					resultSet.getString("id_ville")
				);
				hotels.add(obj);
			}
		} catch (SQLException e) {
			throw new Exception("Error while finding all Hotels: " + e.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new Exception("Error while closing resources: " + e.getMessage());
			}
		}
	
		return hotels;
	}

	public Hotels getById(String id_hotel) throws Exception {
		Hotels hotels = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			connection = Connexion.getConnection();
			String query = "SELECT * FROM Hotels WHERE id_hotel = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1,id_hotel);
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				hotels = new Hotels(
					resultSet.getString("id_hotel"),
					resultSet.getString("nom_hotel"),
					resultSet.getString("description"),
					resultSet.getString("adresse_hotel"),
					resultSet.getDate("date_insertion"),
					resultSet.getString("id_partenaire"),
					resultSet.getString("id_categorie_hotel"),
					resultSet.getString("id_ville")
				);
			}
		} catch (SQLException e) {
			throw new Exception("Error while finding all Hotelss: " + e.getMessage());
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new Exception("Error while closing resources: " + e.getMessage());
			}
		}

		return hotels;
	}

	public void insert() throws Exception {
        
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Connexion.getConnection();
            String query = "INSERT INTO Hotel (id_hotel,nom_hotel,description,adresse_hotel,date_insertion,id_partenaire,id_categorie_hotel,id_ville) VALUES (?,?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, getId_hotel());
            statement.setString(2, getNom_hotel());
            statement.setString(3, getDescription());
            statement.setString(4, getAdresse_hotel());
            statement.setDate(5, getDate_insertion());
            statement.setString(6, getId_partenaire());
            statement.setString(7, getId_categorie_hotel());
            statement.setString(8, getId_ville());
            statement.executeUpdate();
            System.out.println("Donnees Hotel inserer avec succes");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	public void update()throws Exception{
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Connexion.getConnection();
            String query = "UPDATE Hotel SET nom_hotel = ? ,description = ? ,adresse_hotel = ? ,date_insertion = ? ,id_partenaire = ? ,id_categorie_hotel = ? ,id_ville = ?  WHERE id_hotel = ? ";
            statement = connection.prepareStatement(query);
            statement.setString(1, getNom_hotel());
            statement.setString(2, getDescription());
            statement.setString(3, getAdresse_hotel());
            statement.setDate(4, getDate_insertion());
            statement.setString(5, getId_partenaire());
            statement.setString(6, getId_categorie_hotel());
            statement.setString(7, getId_ville());
            statement.setString(8, getId_hotel());
            statement.executeUpdate();
            System.out.println("Donnees Hotel mise a jour avec succes");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }finally{
            try {
                if (statement!=null) {
                    statement.close();
                }
                if (connection!=null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	public void delete(String id_hotel)throws Exception{
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Connexion.getConnection();
            String query = "DELETE  FROM Hotel WHERE id_hotel = ? ";
            statement = connection.prepareStatement(query);
            statement.setString(1, id_hotel);
            statement.executeUpdate();
            System.out.println("Donnees Hotels supprimee avec succes");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }finally{
            try {
                if (statement!=null) {
                    statement.close();
                }
                if (connection!=null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
