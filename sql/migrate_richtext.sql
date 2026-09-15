-- Run after backing up the database. Existing values are retained.
USE ningshang;
ALTER TABLE news MODIFY COLUMN body LONGTEXT;
ALTER TABLE site_content MODIFY COLUMN content LONGTEXT;
ALTER TABLE job MODIFY COLUMN description LONGTEXT;
ALTER TABLE team_member MODIFY COLUMN description LONGTEXT;
ALTER TABLE honor MODIFY COLUMN description LONGTEXT;
ALTER TABLE milestone MODIFY COLUMN description LONGTEXT;
ALTER TABLE subsidiary MODIFY COLUMN description LONGTEXT;
