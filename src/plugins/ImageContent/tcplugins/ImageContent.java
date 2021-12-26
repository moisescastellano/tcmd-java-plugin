package tcplugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

import plugins.wdx.FieldValue;
import plugins.wdx.WDXPluginAdapter;

public class ImageContent extends WDXPluginAdapter {
	/**
	 * the logging support.
	 */
	private Log log = LogFactory.getLog(ImageContent.class);

	/**
	 * The fieldIndex for the pictures type.
	 */
	public static final int IDX_PICTURE_TYPE = 0;

	/**
	 * The fieldIndex for the color depth.
	 */
	public static final int IDX_COLOR_DEPTH = 1;

	/**
	 * The fieldIndex for the x position.
	 */
	public static final int IDX_POS_X = 2;

	/**
	 * The fieldIndex for the y position.
	 */
	public static final int IDX_POS_Y = 3;

	/**
	 * The fieldIndex for the width.
	 */
	public static final int IDX_WIDTH = 4;

	/**
	 * The fieldIndex for the height.
	 */
	public static final int IDX_HEIGHT = 5;

	/**
	 * The fieldIndex for the transparent pixel.
	 */
	public static final int IDX_TRANSPARENT_PIXEL = 6;

	/**
	 * The fieldIndex for the transparency type.
	 */
	public static final int IDX_TRANSPARENCY_TYPE = 7;

	/**
	 * The fieldIndex for the global alpha.
	 */
	public static final int IDX_GLOBAL_ALPHA = 8;

	/**
	 * contentGetSupportedField(): All field names.
	 */
	public static final String[] FIELDNAMES = new String[] { // The type of
	// file from
			// which the
			// image was
			// read.
			"pictureType", //$NON-NLS-1$
			// The color depth of the image, in bits per pixel.
			"colourDepth", //$NON-NLS-1$
			// The x coordinate of the top left corner of the image within the
			// logical screen
			"posX", //$NON-NLS-1$
			// The y coordinate of the top left corner of the image within the
			// logical screen
			"posY", //$NON-NLS-1$
			// The width of the image, in pixels.
			"width", //$NON-NLS-1$
			// The height of the image, in pixels.
			"height", //$NON-NLS-1$
			// The transparent pixel.
			"transparency", //$NON-NLS-1$
			// Returns the image transparency type.
			"transparencyType", //$NON-NLS-1$
			// The global alpha value to be used for every pixel.
			"globalAlpha", }; //$NON-NLS-1$

	/**
	 * contentGetSupportedField(): All field types.
	 */
	public static final int[] FIELDTYPES = new int[] {
	// pictureType
			FieldValue.FT_MULTIPLECHOICE,
			// colourDepth
			FieldValue.FT_NUMERIC_32,
			// posX
			FieldValue.FT_NUMERIC_32,
			// posY
			FieldValue.FT_NUMERIC_32,
			// widthX
			FieldValue.FT_NUMERIC_32,
			// widthY
			FieldValue.FT_NUMERIC_32,
			// transparency
			FieldValue.FT_NUMERIC_32,
			// transparencyType
			FieldValue.FT_MULTIPLECHOICE,
			// globalAlpha
			FieldValue.FT_NUMERIC_32, };

	/**
	 * contentGetSupportedField(): All field names.
	 */
	public static final String[] UNITS_PICTURE_TYPES = new String[] { // 
	// Windows BMP file format, no compression
			"BMP", //$NON-NLS-1$
			// Windows BMP file format, RLE compression if appropriate
			"BMP_RLE", //$NON-NLS-1$
			// GIF file format
			"GIF", //$NON-NLS-1$
			// Windows ICO file format
			"ICO", //$NON-NLS-1$
			// JPEG file format
			"JPEG", //$NON-NLS-1$
			// PNG file format
			"PNG", }; //$NON-NLS-1$

	/**
	 * contentGetSupportedField(): All field names.
	 */
	public static final String[] UNITS_TRANSPARENCEY_TYPES = new String[] { // 
	// No transparency
			"NONE", //$NON-NLS-1$
			// Data from the icon mask.
			"MASK", //$NON-NLS-1$
			// Pixels with this value are transparent.
			"PIXEL", //$NON-NLS-1$
			// The alpha data of the image.
			"ALPHA", }; //$NON-NLS-1$

	/**
	 * listGetDetectString(): All picture extensions.
	 */
	public static final String[] PICTURE_EXTENSIONS = new String[] { // 
	// Windows BMP file format, no compression
			"BMP", //$NON-NLS-1$
			// Windows ICO file format
			"ICO", //$NON-NLS-1$
			// JPEG file format
			"JPG", //$NON-NLS-1$
			// JPEG file format
			"JPEG", //$NON-NLS-1$
			// GIF file format
			"GIF", //$NON-NLS-1$
			// PNG file format
			"PNG", }; //$NON-NLS-1$

	/**
	 * 
	 */
	public ImageContent() {
		if (log.isDebugEnabled()) {
			log.debug("ImageContent.ImageContent()");
		}
	}

	@Override
	public final int contentGetSupportedField(final int fieldIndex,
			final StringBuffer fieldName, final StringBuffer units,
			final int maxlen) {
		if (log.isDebugEnabled()) {
			log
					.debug("ImageContent.contentGetSupportedField(fieldIndex, fieldName, units, maxlen)");
		}
		if (fieldIndex < IDX_PICTURE_TYPE || fieldIndex > IDX_GLOBAL_ALPHA) {
			return FieldValue.FT_NOMOREFIELDS;
		}
		// Set field name
		fieldName.append(FIELDNAMES[fieldIndex]);
		// Set units
		if (fieldIndex == IDX_PICTURE_TYPE) {
			for (int i = 0; i < UNITS_PICTURE_TYPES.length; i++) {
				units.append(UNITS_PICTURE_TYPES[i]);
				if (i != UNITS_PICTURE_TYPES.length - 1) {
					units.append("|"); //$NON-NLS-1$
				}
			}
		} else if (fieldIndex == IDX_TRANSPARENCY_TYPE) {
			for (int i = 0; i < UNITS_TRANSPARENCEY_TYPES.length; i++) {
				units.append(UNITS_TRANSPARENCEY_TYPES[i]);
				if (i != UNITS_TRANSPARENCEY_TYPES.length - 1) {
					units.append("|"); //$NON-NLS-1$
				}
			}
		}
		return FIELDTYPES[fieldIndex];
	}

	@SuppressWarnings("unchecked")
	@Override
	public final int contentGetValue(final String fileName,
			final int fieldIndex, final int unitIndex,
			final FieldValue fieldValue, final int maxlen, final int flags) {
		if (log.isDebugEnabled()) {
			log
					.debug("ImageContent.contentGetValue(fileName, fieldIndex, unitIndex, fieldValue, maxlen, flags)");
		}
		boolean correctExtension = false;
		for (int i = 0; i < PICTURE_EXTENSIONS.length; i++) {
			if (fileName.toUpperCase().endsWith("." //$NON-NLS-1$
					+ PICTURE_EXTENSIONS[i])) {
				correctExtension = true;
			}
		}
		if (!correctExtension) {
			if (log.isDebugEnabled()) {
				log.debug("ImageContent.contentGetValue()" //$NON-NLS-1$
						+ " INCORRECT EXTENSION: " + fileName); //$NON-NLS-1$
			}
			return FieldValue.FT_FIELDEMPTY;
		}
		try {
			// Cache picture data
			ImageData data;
			ImageLoader loader = new ImageLoader();
			try {
				loader.load(fileName);
			} catch (RuntimeException e) {
				log.error(e.getMessage(), e);
				return FieldValue.FT_FILEERROR;
			}
			data = loader.data[0];
			if (log.isDebugEnabled()) {
				log.debug("ImageContent.contentGetValue()" //$NON-NLS-1$
						+ " CACHED: " //$NON-NLS-1$
						+ fileName);
			}
			// Set field data
			int fieldType = FIELDTYPES[fieldIndex];
			switch (fieldIndex) {
			case IDX_PICTURE_TYPE:
				fieldValue.setValue(fieldType, UNITS_PICTURE_TYPES[data.type]);
				break;

			case IDX_COLOR_DEPTH:
				fieldValue.setValue(fieldType, new Integer(data.depth));
				break;

			case IDX_POS_X:
				fieldValue.setValue(fieldType, new Integer(data.x));
				break;

			case IDX_POS_Y:
				fieldValue.setValue(fieldType, new Integer(data.y));
				break;

			case IDX_WIDTH:
				fieldValue.setValue(fieldType, new Integer(data.width));
				break;

			case IDX_HEIGHT:
				fieldValue.setValue(fieldType, new Integer(data.height));
				break;

			case IDX_TRANSPARENT_PIXEL:
				fieldValue.setValue(fieldType, new Integer(
						data.transparentPixel));
				break;

			case IDX_TRANSPARENCY_TYPE:
				fieldValue.setValue(fieldType, UNITS_TRANSPARENCEY_TYPES[data
						.getTransparencyType()]);
				break;

			default:
				break;
			}
			return fieldType;
		} catch (RuntimeException e) {
			log.error(e.getMessage(), e);
			return FieldValue.FT_FILEERROR;
		}
	}

	@Override
	public final String contentGetDetectString(final int maxLen) {
		if (log.isDebugEnabled()) {
			log.debug("ImageContent.contentGetDetectString(maxLen)");
		}
		StringBuffer detectString = new StringBuffer();
		detectString.append("FORCE | MULTIMEDIA | "); //$NON-NLS-1$
		for (int i = 0; i < PICTURE_EXTENSIONS.length; i++) {
			detectString.append("EXT= \"" //$NON-NLS-1$ 
					+ PICTURE_EXTENSIONS[i] + "\""); //$NON-NLS-1$
			if (i != PICTURE_EXTENSIONS.length - 1) {
				detectString.append(" | "); //$NON-NLS-1$
			}
		}
		return detectString.toString();
	}
}
