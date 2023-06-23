## Android app "VK ENJOYER"
### Idea
The key idea of the application is to enhance the user experience when working with vk's recommendation feed giving it a new breath. 

### Stack

* __Jetpack compose__ for user inteface 
* __Kotlin flows + coroutines__ for asynchronous operations 
* __Dagger2__ for dependency injection 
* __Room__ to store data locally 
* __Retrofit__ for api calls
* __Picasso__ to display images
* __MVVM__ architecture

vk api: https://dev.vk.com/reference

### Detailed description
"VK enjoyer" has 5 sections:
1. Authorization
2. Main (recommendations)
3. Main (comments)
4. Favorites
5. Profile 

#### 1. Authorization screen
In the authorization screen the user sign in or sign up to their account using VK ID.
![auth_screen](https://github.com/VladislavDobrihlopez/VkNewsClient/tree/develop/imgs/vk_auth.gif)

#### 2. Recommendations screen
The whole screen is a feed post the user can scroll. There are interactive metrics at the end of each post: the number of views, shares, comments, likes.
So, the user can share the post on their, like it or go to the Comments screen.
It's not over yet. The post can be ignored (that say it will never appear in the post feed again) and cached. A pop up window appears with the list of availiable tags that can be assosiated to the post.
![recommendations_screen](https://github.com/VladislavDobrihlopez/VkNewsClient/tree/develop/imgs/vk_recommendations_feed.gif)

#### 3. Comments screen
The screen is simple as 1,2,3...
It provides a list of comments to the post the user can scroll and read.
![comments_screen](https://github.com/VladislavDobrihlopez/VkNewsClient/tree/develop/imgs/vk_comments.gif)

#### 4. Favorites
Here the user can filter cached posts by chosen tags and retrieve accordingly results.
A tag can be created and deleted. All the posts with the tag the user is about to delete, are also going to be deleted.
If you wish, you might delete posts you don't wan't to be cached anymore by swiping.
![favorites_screen](https://github.com/VladislavDobrihlopez/VkNewsClient/tree/develop/imgs/vk_favorites.gif)

#### 5. Profile screen

In the profile screen the user can see brief information about someone and more detailed information clicking on "more details". There is also scrollable user's wall
![profile_screen](https://github.com/VladislavDobrihlopez/VkNewsClient/tree/develop/imgs/vk_profile.gif)

### Theming

Also app supports both dark and light theme

![theming](https://github.com/VladislavDobrihlopez/VkNewsClient/tree/develop/imgs/vk_theming.gif)

#### Conclusion
In overall, this app provides not only the ability to view the recommendation post feed in the concise way but also the ability to cache posts assosiating them with tags in the user's mobile phone storage. 
