Introduction of the assignment1:
    For aliens:
        I create a class AlienClazz to encapsulate the code which are used to create the body sphere and build them
        together by hierarchies. In this case, you just need to declare 2 object of SGNode in the
        ALIENS_GLEventListener.java to create 2 aliens. Also, I use different textures to cover the different parts of
        the body, and diffuse and specular maps on the main body.
    For backdrops:
        The backdrop consists of 2 parts: the back and the floor. In order to make the 2 planes join smoothly, I cut
        one portrait picture into 2 parts, one for the back and one for the floor. The snow falling animation is made by
        combining snowing texture to the vertical plane texture. Then creating a function called snow() to change the
        offset in the moving texture coordinate by time.
    For security spotlight:
        The spotlight is made of transformed spheres which contains light post, light shade and bulb. The upper brand
        (light shade & bulb) is designed to rotate around X and Z axis, and then rotate to the bottom right to illuminate
        the scene. I also use a boolean value 'bulbOn' in the fragment shader of bulb(fs_bulb.txt) to change the
        different color of the bulb when turning on or off the worlds lights.
        The spotlight effect is made by another light which has a similar movement with bulb. The sphere of the bulb
        is an instance of ModelMultipleLights class, not an instance of Light class, which means it cannot glow on its own.
        So I create an instance of Bulb(a subclass of Light class) to simulate the illumination of the bulb.
    For animation:
        Both of the 2 aliens can rock their heads and bodies. Though they are moving simultaneously, the 'rock body' and
        'rock head' can run separately. And I add 2 properties to count the parse time which allows the animation can
        continue smoothly when clicking the 'rock' button next time.
    For lighting:
        I change the Model class to the ModelMultipleLights which can render a scene with more than one light source.
        One parameter of ModelMultipleLights is an array of Light instances which values will be injected in to corresponding
        fragment shader in a for-loop then. Also, I use a parameter 'type' in struct Light to distinguish between the general light and
        the spotlight. The final result of fragment color will be produced by adding these light illuminations.