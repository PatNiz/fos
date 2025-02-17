SELECT c.Name 
FROM Country c
LEFT JOIN City ci ON c.CountryID = ci.CountryID
LEFT JOIN Building b ON ci.CityID = b.CityID
GROUP BY c.CountryID
HAVING COUNT(b.BuildingID) = 0;

SELECT c.Name 
FROM Country c
JOIN City ci ON c.CountryID = ci.CountryID
GROUP BY c.CountryID
HAVING SUM(ci.Population) > 400;