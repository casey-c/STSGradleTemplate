package GradleTemplate;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class GradleTemplate implements PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(GradleTemplate.class);

    public static void initialize() {
        new GradleTemplate();
    }

    public GradleTemplate() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");
    }
}