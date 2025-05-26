# university-belt-express
## Student-Friendly Food Delivery App  

## Overview  
- **U-Belt-ExPress** is a full-stack food delivery application developed specifically for students within the University Belt (U-Belt) area of Manila. Taking inspiration from established platforms like Foodpanda, it aims to provide a fast, reliable, and student-friendly way to order food from nearby local restaurants, eateries, and food stalls. The platform is designed with the student lifestyle in mind—prioritizing affordability, convenience, and ease of use.
- The project is currently in the active development stage, with both frontend and backend systems being constructed in parallel. Efforts are focused on building the app's foundational structure, which includes user interface design, database schema development through an Entity Relationship Diagram (ERD), and essential functionalities such as restaurant listings, order placement, and a built-in e-wallet for cashless transactions.
- To ensure real-time relevance and pricing accuracy, UBELT-ExPress incorporates web-scraped data from restaurant pages and menus. This allows the platform to dynamically reflect menu offerings and prices from partner establishments within the U-Belt area, improving the overall accuracy and usefulness of the app for its student users.

(This project is not intended to create and deliver actual orders.)

## Purpose  
The goal of UBELT-ExPress is to:  
- Provide a **student-focused food delivery platform** with an easy-to-use interface. 
- Create a working **database structure** to handle users, partner restaurants, and order data.  
- Explore **backend systems** that simulate a real-time food ordering process.  

## Features (Planned)  
- **User Authentication** – Sign up, log in, and manage profiles.  
- **Restaurant Listings** – Browse available restaurants and menus.  
- **Order Management** – Place, track, and manage food orders.
- **E-Wallet Integration** – Manage in-app payments and balance.
- **Manage Restaurant** - Login as business owner. Manage stock quantity.
- **Estimated Time of Delivery** - The Google Maps API offers Distance Matrix that allows for the ETA from point A to B including traffic time.
- **Personalized Messages** - The OpenAI API will customize the short messages to notify deliveries and orders.

## Technologies Used  
- **Backend:** Java, Python
- **Database:** MySQL, Microsoft SQL
- **UI/UX:** Figma and Scene Buidler
- **IDE:** VScode
- **Tools:** Git, Github
- **Other Languages:** Open to future additions as needed  

## Installation  
1. Clone this repository:  
   ```sh  
   git clone https://github.com/rpaulos/ubeltExpress  
   ```  
2. Set up the project environment.
   - Setup Anaconda
   - Install python, googlemaps, OpenAI, Selenium, and Undetected Chrome Driver
4. Configure the MySQL database using the provided schema (See the sql_script).  
5. Run the application in your Java development environment.  

## Usage  
1. Navigate through the UI to explore restaurant listings (once implemented).  
2. Place an order from the menu (future feature).  
3. Track your order status (future feature).  

## Future Enhancements  
- **Web Scraping** - Gather data from the websites of various local restaurants nearby.
  

## Contributors  
- **[Rae Paulos](https://github.com/rpaulos)** – Lead Frontend, Backend Developer, & Customer Side
- **[Tristan Sevilla](https://github.com/Hyakkki)** - Frontend, Backend Developer & Business Side
- **[Jared Pilapil](https://github.com/RedJared)** - Frontend, Backend Developer & Admin Side

## License  
This project is for **personal and educational purposes** and is not intended for commercial use.  
