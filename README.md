# My Personal Project - Films and Friends 🍿🎬

## A way to rate ⭐️, catalogue 📚, and get movie recommendations from your friends 🫶

Features:
- add a movie (title, director, genre, date watched) to your **personal watched list**
- form a "group" of friends to *view each other's lists and reviews*
- give watched movies a *rating out of 5*; a *group rating* for the movie will be calculated 
based on you and your friends' ratings

Who will use it:
<p>Any group of friends who enjoy films together and want an easier way to share their love for it!
A great way to decide what to re-watch on group movie night or what new movie to watch based on friends' 
recommendations.</p>

My inspiration:
<p>My friends and I love talking about movies but never had a dedicated place to view or keep track of all the 
movies we've watched and what we thought of them. There is a similar social media platform for films called Letterboxd 
that I've used, but I can see myself using this with a close group of friends.</p>

## User Stories ##
- As a user, I want to add a movie and its rating to my watched list
- As a user, I want to add to my watch list
- - As a user, I want to remove a movie from my watch list
- As a user, I want to see a movie's group rating based on my friends' ratings
- As a user, I want to save my to-watch list to  (if I so choose)
- As a user, I want to save my watched list to file (if I so choose)
- As a user, I want to view my saved to-watch list (if I so choose)
- As a user, I want to view my saved watched list (if I so choose)

# Instructions for Grader

- You can generate the first required action related to adding movies(Xs) to watchlist(Y) by clicking "add movie" button
after you have selected a user
- You can generate the second required action related to removing movies(Xs) from watchlist(Y) by clicking "remove" button
for an existing movie on the user's watchlist
- You can generate an (optional) third action related to removing movies(Xs) from watchlist(Y) and adding to your 
watched list (Z) by clicking on the "watch/review" button
- You can locate my visual component by looking at the "group movie ratings" window smiley faces
- You can save the state of my application by clicking "save current users to file" in the file menu
- You can reload the state of my application by "load saved users from file" in the file menu

## Phase 4: Task 2
Added User 1 to system. at Thu Apr 13 12:51:00 PDT 2023
Added movie: new movie at Thu Apr 13 12:51:15 PDT 2023
Removed movie: new movie at Thu Apr 13 12:51:18 PDT 2023

## Phase 4: Task 3
I would definitely refactor the design of my Action classes for redundancy by 
making an Action class (possibly abstract) that can be implemented by subclasses and 
have more specific implementations for their intended purpose to avoid duplicate code. 
Or, via constructor/method parameters I could have one Action class that has different 
implementations for each purpose based on the passed in parameter.

Refactoring to incorporate the Observer Pattern would be another way to improve my 
design between the "groupMovies" WatchedMovieList field and "users" UserList field in MovieSystem so 
that when users add a new WatchedMovie to their own watched lists, it can notify and update the 
groupMovies list to add a new movie and calculate the group rating for each movie. In this case, 
the ConcreteSubject would be the UserList and the ConcreteObserver would be the WatchedMovieList. I would also
have to change the fields in UserList so that WatchedMovieList/Observer would have easier
access to all the WatchedMovie for each User.