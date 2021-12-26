// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ObjLoad.java

package tcplugins;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.mnstarfire.loaders3d.Inspector3DS;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class ObjLoad extends JPanel
{

    public TransformGroup load3DS(String filename)
    {
        Transform3D rotate = new Transform3D();
        rotate.rotX(0.78539816339744828D);
        rotate.setScale(4);
        Inspector3DS loader = new Inspector3DS(filename);
        loader.setLogging(false);
        loader.setDetail(6);
        loader.setTextureLightingOn();        
        loader.parseIt();
//        loader.setTexturePath("c:/");
        TransformGroup theModel = new TransformGroup(rotate);
        theModel = loader.getModel();
        return theModel;
    }

    public BranchGroup createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.0001D);
//        t3d.setScale(0.001D);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);
        objTrans = load3DS(filename.getFile());
		objTrans.setCapability(18);
        objTrans.setCapability(17);
        objScale.addChild(objTrans);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0D, 0.0D, 0.0D), 100D);
        if(spin)
        {
            Transform3D yAxis = new Transform3D();
            Alpha rotationAlpha = new Alpha(-1, 1, 0L, 0L, 4000L, 0L, 0L, 0L, 0L, 0L);
            RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objTrans, yAxis, 0.0F, 6.283185F);
            rotator.setSchedulingBounds(bounds);
            objTrans.addChild(rotator);
        }
        Color3f bgColor = new Color3f(0.05F, 0.05F, 0.5F);
        Background bgNode = new Background(bgColor);
        bgNode.setApplicationBounds(bounds);
        objRoot.addChild(bgNode);
        return objRoot;
    }

    private Canvas3D createUniverse()
    {
        java.awt.GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3d = new Canvas3D(config);
        univ = new SimpleUniverse(canvas3d);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0D, 0.0D, 0.0D), 100D);
        ViewingPlatform viewingPlatform = univ.getViewingPlatform();
        PlatformGeometry pg = new PlatformGeometry();
        Color3f ambientColor = new Color3f(0.1F, 0.1F, 0.1F);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        pg.addChild(ambientLightNode);
        Color3f light1Color = new Color3f(1.0F, 1.0F, 0.9F);
        Vector3f light1Direction = new Vector3f(1.0F, 1.0F, 1.0F);
        Color3f light2Color = new Color3f(1.0F, 1.0F, 1.0F);
        Vector3f light2Direction = new Vector3f(-1F, -1F, -1F);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        pg.addChild(light1);
        DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        pg.addChild(light2);
        viewingPlatform.setPlatformGeometry(pg);
        viewingPlatform.setNominalViewingTransform();
        if(!spin)
        {
            OrbitBehavior orbit = new OrbitBehavior(canvas3d, 112);
            orbit.setSchedulingBounds(bounds);
            viewingPlatform.setViewPlatformBehavior(orbit);
        }
        univ.getViewer().getView().setMinimumFrameCycleTime(5L);
        return canvas3d;
    }

    private void usage()
    {
        System.out.println("Usage: java ObjLoad [-s] [-n] [-t] [-c degrees] <.obj file>");
        System.out.println("  -s Spin (no user interaction)");
        System.out.println("  -n No triangulation");
        System.out.println("  -t No stripification");
        System.out.println("  -c Set crease angle for normal generation (default is 60 without");
        System.out.println("     smoothing group info, otherwise 180 within smoothing groups)");
        System.exit(0);
    }

    public ObjLoad(String args[])
    {
        spin = false;
        noTriangulate = false;
        noStripify = false;
        creaseAngle = 60D;
        filename = null;
        univ = null;
        scene = null;
        if(args.length != 0)
        {
            for(int i = 0; i < args.length; i++)
                if(args[i].startsWith("-"))
                {
                    if(args[i].equals("-s"))
                        spin = true;
                    else
                    if(args[i].equals("-n"))
                        noTriangulate = true;
                    else
                    if(args[i].equals("-t"))
                        noStripify = true;
                    else
                    if(args[i].equals("-c"))
                    {
                        if(i < args.length - 1)
                            creaseAngle = (new Double(args[++i])).doubleValue();
                        else
                            usage();
                    } else
                    {
                        usage();
                    }
                } else
                {
                    try
                    {
                        if(args[i].indexOf("file:") == 0 || args[i].indexOf("http") == 0)
                            filename = new URL(args[i]);
                        else
                        if(args[i].charAt(0) != '/')
                            filename = new URL("file:./" + args[i]);
                        else
                            filename = new URL("file:" + args[i]);
                    }
                    catch(MalformedURLException e)
                    {
                        System.err.println(e);
                        System.exit(1);
                    }
                }

        }
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 700));
        Canvas3D c = createUniverse();
        add(c, "Center");
        scene = createSceneGraph();
        univ.addBranchGraph(scene);
    }

    public static void main(String args1[])
    {
    }

    private boolean spin;
    private boolean noTriangulate;
    private boolean noStripify;
    private double creaseAngle;
    private URL filename;
    private SimpleUniverse univ;
    private BranchGroup scene;
	private TransformGroup objTrans;
	public TransformGroup getObjTrans() {
		return objTrans;
	}
}
