Minimal MVVM/Architecture Components based app sample that displays list of pictures from network.

[![Demo video](https://github.com/ConstantineMars/HsExercise/blob/master/files/Screen%20Shot%202020-03-19%20at%2011.24.08%20PM.png)](https://youtu.be/2CQyPyOEeoM)

The goal of this problem is to create an Android app that displays a list of photos from the picsum API ([https://picsum.photos/](https://picsum.photos/))

The endpoint used is [https://picsum.photos/v2/list](https://picsum.photos/v2/list)

Functionality:
- Displays the image, author, and dimensions of the photos
- Vertical orientation or a grid
- Handles the following states :
    - Empty State (no data)
    - Error State (api call failed)
    - Loading State (api call is taking place)
    - Content State (there is data to display)
- Offline mode
